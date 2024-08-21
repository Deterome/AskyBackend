package org.senla_project.application.dao;

import lombok.NonNull;
import org.apache.commons.beanutils.PropertyUtils;
import org.senla_project.application.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Dao<T extends Entity & Cloneable> {

    protected List<T> entities = new ArrayList<>();
    private final Class<T> entityClass;

    public Dao(@NonNull Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public List<T> findAll() {
        return entities.stream()
                .map(entity -> entityClass.cast(entity.clone()))
                .collect(Collectors.toList());
    }

    public Optional<T> findById(@NonNull UUID id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .map(entity -> entityClass.cast(entity.clone()));
    }

    public void create(@NonNull T entity) {
        entities.add(entity);
    }

    public void update(@NonNull UUID id, @NonNull T updatedEntity) {
        updatedEntity.setId(id);
        try {
            for (T entity: entities) {
                if (entity.getId().equals(id)) {
                        PropertyUtils.copyProperties(entity, updatedEntity);
                        break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(@NonNull UUID id) {
        entities.removeIf(entity -> entity.getId().equals(id));
    }
}
