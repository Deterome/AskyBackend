package org.senla_project.application.service;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceInterface<T, R> {
    void execute();

    List<R> getAllElements();
    Optional<R> getElementById(@NonNull UUID id);
    void addElement(@NonNull T element);
    void updateElement(@NonNull UUID id, @NonNull T newElement);
    void deleteElement(@NonNull UUID id);

}
