package org.senla_project.application.controller;

import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DefaultControllerInterface<K, T, R> {
    Page<R> getAll(int pageNumber, int pageSize);

    R getById(K id);

    R create(@NonNull T element);

    R update(@NonNull K id, @NonNull T updatedElement);

    void delete(@NonNull K id);
}
