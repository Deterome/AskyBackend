package org.senla_project.application.service;

import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public interface ServiceInterface<T> {
    void execute();

    @NonNull
    List<T> getAllElements();
    @Nullable
    T getElementById(UUID id);
    void addElement(@NonNull T element);
    void updateElement(@NonNull UUID id, @NonNull T newElement);
    void deleteElement(@NonNull T element);

}
