package org.senla_project.application.service;

import lombok.NonNull;

import java.util.List;

public interface ServiceInterface<K, T, R> {
    List<R> findAllElements(int pageNumber);

    R findElementById(@NonNull K id);

    R addElement(@NonNull T element);

    R updateElement(@NonNull K id, @NonNull T updatedElement);

    void deleteElement(@NonNull K id);

}
