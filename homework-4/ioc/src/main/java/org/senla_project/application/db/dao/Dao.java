package org.senla_project.application.db.dao;

import lombok.NonNull;
import org.senla_project.application.db.entities.Entity;

import java.util.List;
import java.util.UUID;

public abstract class Dao<E extends Entity> {

    protected List<E> entities;

    public List<E> findAll() {
        return entities;
    }

    public E findById(@NonNull UUID id) {
        for (var entity: entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    public void save(E entity) {
        entities.add(entity);
    }

    public void delete(E entity) {
        entities.remove(entity);
    }

}
