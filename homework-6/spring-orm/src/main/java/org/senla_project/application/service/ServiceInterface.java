package org.senla_project.application.service;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface ServiceInterface<K, T, R> {
    void execute();

    List<R> getAllElements();
    Optional<R> getElementById(@NonNull K id);
    void addElement(@NonNull T element);
    void updateElement(@NonNull K id, @NonNull T updatedElement);
    void deleteElement(@NonNull K id);

}
