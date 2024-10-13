package org.senla_project.application.repository;

import java.util.List;
import java.util.Optional;

public interface DefaultDao<K, T> {

    T create(T entity);

    Optional<T> findById(K id);

    List<T> findAll();

    T update(T updatedEntity);

    void deleteById(K id);

}
