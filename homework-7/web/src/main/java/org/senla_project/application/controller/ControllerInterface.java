package org.senla_project.application.controller;

import lombok.NonNull;

import java.util.List;

public interface ControllerInterface<K, T, R> {
    List<R> getAllElements();
    R findElementById(K id);
    void addElement(@NonNull T element);
    void updateElement(@NonNull K id, @NonNull T updatedElement);
    void deleteElement(@NonNull K id);
}
