package org.senla_project.application.controller;

import lombok.NonNull;

import java.util.List;

public interface ControllerInterface<K, T, R> {
    List<R> getAllElements();
    R getElementById(K id);
    R addElement(@NonNull T element);
    R updateElement(@NonNull K id, @NonNull T updatedElement);
    void deleteElement(@NonNull K id);
}
