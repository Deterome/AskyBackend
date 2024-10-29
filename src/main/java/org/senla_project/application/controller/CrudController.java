package org.senla_project.application.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface CrudController<T, R, D, ID> {
    Page<R> getAll(@Positive @Min(1) int pageNumber, @Positive @Min(1) int pageSize);

    R getById(ID id);

    R create(@NonNull T element);

    R update(@NonNull ID id, @NonNull T updatedElement);

    void delete(@NonNull D deleteDto);
}
