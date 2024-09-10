package org.senla_project.application.repository;

import java.util.List;
import java.util.Optional;

public interface DefaultDao<K, T> {

    void create(T entity);
    Optional<T> findById(K id);
    List<T> findAll();
    void update(T updatedEntity);
    void deleteById(K id);

}
