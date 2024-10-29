package org.senla_project.application.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<T, R, D, ID> {
    Page<R> getAll(Pageable pageable);

    R getById(@NonNull ID id);

    R create(@NonNull T createDto);

    R updateById(@NonNull ID id, @NonNull T updatedElement);

    void delete(@NonNull D deleteDto);

}
