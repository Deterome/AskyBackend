package org.senla_project.application.controller;

import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public interface ControllerInterface<T> {
    void execute();

    String getAllElements();
    String getElementById(UUID id);
    void addElement(@NonNull T element);
    void updateElement(@NonNull UUID id, @NonNull T newElement);
    void deleteElement(@NonNull UUID id);
}
