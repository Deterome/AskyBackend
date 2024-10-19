package org.senla_project.application.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceInterface<ID, T, R> {
    Page<R> getAll(Pageable pageable);

    R getById(@NonNull ID id);

    R create(@NonNull T element);

    R updateById(@NonNull ID id, @NonNull T updatedElement);

    void deleteById(@NonNull ID id);

}
