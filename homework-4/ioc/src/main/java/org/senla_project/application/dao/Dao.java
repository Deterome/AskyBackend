package org.senla_project.application.dao;

import lombok.NonNull;
import org.apache.commons.beanutils.PropertyUtils;
import org.senla_project.application.entity.Entity;
import org.springframework.lang.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Dao<T extends Entity & Cloneable> {

    protected List<T> entities = new ArrayList<>();
    Class<T> entityClass;

    public Dao(@NonNull Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @NonNull
    public List<T> findAll() {
        return entities.stream().map(entity -> entityClass.cast(entity.clone())).collect(Collectors.toList());
    }

    @Nullable
    public T findById(@NonNull UUID id) {
        for (T entity: entities) {
            if (entity.getId().equals(id)) {
                return entityClass.cast(entity.clone());
            }
        }
        return null;
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
