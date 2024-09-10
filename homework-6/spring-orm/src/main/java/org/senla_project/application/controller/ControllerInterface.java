package org.senla_project.application.controller;

import lombok.NonNull;

import java.util.UUID;

public interface ControllerInterface<K, T> {
    void execute();

    String getAllElements();
    String getElementById(K id);
    void addElement(@NonNull T element);
    void updateElement(@NonNull K id, @NonNull T updatedElement);
    void deleteElement(@NonNull K id);
}
