package org.senla_project.application.repository;

import java.util.List;
import java.util.Optional;

public interface DefaultDao<K, T> {

    void create(T entity);
    Optional<T> getById(K id);
    List<T> getAll();
    void updateById(T updatedEntity);
    void deleteById(K id);

}
