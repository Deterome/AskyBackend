package org.senla_project.application.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<C, R, U, D, ID> {
    Page<R> getAll(Pageable pageable);

    R getById(@NonNull ID id);

    R create(@NonNull C createDto);

    R updateById(U updateDto);

    void delete(@NonNull D deleteDto);

}
