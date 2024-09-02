package org.senla_project.application.dao;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DefaultDao<T> {

    void create(@NonNull T entity);
    void update(@NonNull UUID id, @NonNull T updatedEntity);
    void deleteById(@NonNull UUID id);
    List<T> findAll();
    Optional<T> findById(@NonNull UUID id);

}
