package org.senla_project.annotations;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.senla_project.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class IocContext {

    public IocContext(String packageToScan) {
        initComponentContainer(packageToScan);
    }

    private void initComponentContainer(String packageToScan) {
        try {
            fillContainerWithComponentsFromPackage(packageToScan);
            initComponentFields();
        } catch (InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillContainerWithComponentsFromPackage(String packageToScan) throws
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        Reflections reflections = new Reflections(packageToScan);
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

    private void initComponentFields() throws IllegalAccessException, InvocationTargetException {
        for (var component: container.values()) {
            processComponentFields(component);
            processComponentSetters(component);
        }
    }

    private void processComponentFields(@NotNull Object component) throws IllegalAccessException {
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

    private void initFieldWithValue(@NotNull Object component,@NotNull Field field, String value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(component, value);
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
    private final Map<String, Object> container = new HashMap<>();
}