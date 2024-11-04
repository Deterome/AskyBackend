package org.senla_project.application.util.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public abstract class StringToEnumConvertor<E extends Enum<?>> implements Converter<String, E> {

    private final Class<E> enumType;

    @Override
    public E convert(String source) {
        try {
            String formattedSource = source.replaceAll("(?<!^)([A-Z])", "_$1").toUpperCase();
            Method valueOfMethod = enumType.getMethod("valueOf", String.class);
            return (E) valueOfMethod.invoke(null, formattedSource);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert string to enum", e);
        }
    }
}
