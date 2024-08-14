package org.senla_project.ioc.context;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.senla_project.ioc.annotations.Autowire;
import org.senla_project.ioc.annotations.Component;
import org.senla_project.ioc.annotations.Value;
import org.senla_project.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IocContext {

    public IocContext(String contextPackage) {
        this.contextPackage = contextPackage;
        initComponentContainer();
    }

    private void initComponentContainer() {
        try {
            fillContainerWithComponentsFromPackage();
            initComponentFields();
        } catch (InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 InvocationTargetException |
                 IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillContainerWithComponentsFromPackage() throws
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        Reflections reflections = new Reflections(contextPackage);
        Queue<Class<?>> uncreatedComponentClasses = new LinkedList<>(reflections.getTypesAnnotatedWith(Component.class));
        //   Создаём компоненты в цикле. Цикл завершается в том случае, если мы создали все компоненты или возникла ситуация,
        //  когда мы не можем создать компоненты из-за их зависимости друг от друга.
        for (int lastQueueSize = uncreatedComponentClasses.size(), idleIterations = 0;
             !uncreatedComponentClasses.isEmpty() ||
                     (lastQueueSize != uncreatedComponentClasses.size() && idleIterations == uncreatedComponentClasses.size());
             idleIterations++) {
            lastQueueSize = uncreatedComponentClasses.size();
            var componentClass = uncreatedComponentClasses.poll();

            var constructorsOfComponent = findConstructorsToCreateComponent(componentClass);
            List<AbstractMap.SimpleEntry<Constructor<?>, List<Object>>> appropriateConstructorsWithArgsListOfComponent = new ArrayList<>();

            //   Ищем конструкторы, для которых можно составить список аргументов из существующих компонентов
            for (var constructor: constructorsOfComponent) {
                List<Object> argsList = new ArrayList<>();
                if (!tryMakeArgsListForAutowireConstructor(constructor, argsList)) continue;
                appropriateConstructorsWithArgsListOfComponent.add(new AbstractMap.SimpleEntry<>(constructor, argsList));
            }

            if (appropriateConstructorsWithArgsListOfComponent.isEmpty()) {
                //  Если мы не смогли составить список аргументов для конструктора,
                // то, возможно, данному компоненту нужны ещё не созданные компоненты.
                //  Поэтому откладываем создание компоненты на потом, перенося его в конец очереди.
                uncreatedComponentClasses.offer(componentClass);
            } else if (appropriateConstructorsWithArgsListOfComponent.size() == 1) {
                //   Если у нас есть только один подходящий конструктор, то создаём компонент с помощью него
                var constructor = appropriateConstructorsWithArgsListOfComponent.getFirst().getKey();
                var argsList = appropriateConstructorsWithArgsListOfComponent.getFirst().getValue();

                constructor.setAccessible(true);
                if (!argsList.isEmpty()) {
                    container.put(StringUtils.lowercaseFirst(componentClass.getSimpleName()),
                            constructor.newInstance(argsList.toArray()));
                } else {
                    container.put(StringUtils.lowercaseFirst(componentClass.getSimpleName()),
                            constructor.newInstance());
                }
                //  Обнуляем счётчик холостых итераций.
                idleIterations = 0;
            } else {
                throw new RuntimeException(String.format(
                        "Ambiguous autowire constructor. " +
                                "There is more than 1 appropriate autowire constructor for class %s." +
                                "Unable to create component.",
                        componentClass.getName()));
            }
        }
        if (!uncreatedComponentClasses.isEmpty())
            throw new RuntimeException(String.format(
                    "Unable to create components %s", uncreatedComponentClasses.toString()
            ));
    }

    private List<Constructor<?>> findConstructorsToCreateComponent(Class<?> componentClass) throws NoSuchMethodException {
        List<Constructor<?>> constructors = new ArrayList<>();

        for (var componentConstructor: componentClass.getDeclaredConstructors()) {
            if (componentConstructor.isAnnotationPresent(Autowire.class))
                constructors.add(componentConstructor);
        }

        if (constructors.isEmpty()) {
            constructors.add(componentClass.getDeclaredConstructor()); // add default constructor
        }

        return constructors;
    }

    private boolean tryMakeArgsListForAutowireConstructor(@NotNull Constructor<?> constructor,@NotNull List<Object> destArgsList) {
        List<Object> srcArgsList = new ArrayList<>();
        for (var parameter: constructor.getParameterTypes()) {
            var component = findComponentByType(parameter);
            if (component == null) return false;
            srcArgsList.add(component);
        }
        destArgsList.clear();
        destArgsList.addAll(srcArgsList);
        return true;
    }

    private void initComponentFields() throws IllegalAccessException, InvocationTargetException, IOException {
        for (var component: container.values()) {
            processComponentFields(component);
            processComponentSetters(component);
        }
    }

    private void processComponentFields(@NotNull Object component) throws IllegalAccessException, IOException {
        for (var field: component.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Value.class))
                initFieldWithValue(component, field, field.getAnnotation(Value.class).value());
            if (field.isAnnotationPresent(Autowire.class))
                autowireComponentField(component, field);
        }
    }

    private void processComponentSetters(@NotNull Object component) throws InvocationTargetException, IllegalAccessException {
        for (var method: component.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowire.class)) {
                if (method.getName().startsWith("set")) {
                    autowireComponentFieldBySetter(component, method);
                }
            }
        }
    }

    private void autowireComponentFieldBySetter(@NotNull Object component, @NotNull Method setter) throws InvocationTargetException, IllegalAccessException {
        var componentToWire = findComponentByType(setter.getParameterTypes()[0]);

        setter.invoke(component, componentToWire);
    }

    private void autowireComponentField(@NotNull Object component, @NotNull Field field) throws IllegalAccessException {
        var componentToWire = findComponentByType(field.getType());
        if (componentToWire != null) {
            field.setAccessible(true);
            field.set(component, componentToWire);
        }
    }

    private void initFieldWithValue(@NotNull Object component, @NotNull Field field, String value) throws IllegalAccessException, IOException {
        Pattern variableRegexPattern = Pattern.compile("\\$\\{([A-Za-z0-9.]+)}");
        Matcher variableMatcher = variableRegexPattern.matcher(value);
        if (variableMatcher.find()) {
            value = findVariableInPropertyFile(variableMatcher.group(1));
        }
        field.setAccessible(true);
        field.set(component, value);
    }

    private String findVariableInPropertyFile(String variableName) throws IOException {
        InputStream propertiesFileStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

        Properties properties = new Properties();
        properties.load(propertiesFileStream);

        return properties.getProperty(variableName);
    }

    public <T> T findComponent(String componentName, @NotNull Class<T> componentClass) {
        var component = container.get(componentName);

        if (component != null) return componentClass.cast(component);
        else throw new RuntimeException(String.format("There is no component with name %s!", componentName));
    }

    public <T> T findComponentByType(@NotNull Class<T> type) {
        for (var component: container.values()) {
            if (type.isInstance(component)) return type.cast(component);
        }
        return null;
    }

    private final String propertiesFileName = "application.properties";
    private final String contextPackage;
    private final Map<String, Object> container = new HashMap<>();
}