package org.senla_project.application.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.senla_project.application.util.sort.SortOrder;
import org.springframework.data.domain.Page;

public interface CrudController<C, R, U, D, ID, S> {

    Page<R> getAll(@Positive @Min(1) int pageNumber, @Positive @Min(1) int pageSize, S sortType, SortOrder sortOrder);

    R getById(@NonNull ID id);

    R create(@NonNull C createDto);

    R update(@NonNull U updateDto);

    void delete(@NonNull D deleteDto);

}
