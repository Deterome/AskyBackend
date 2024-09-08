package org.senla_project.application.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<K, T> implements DefaultDao<K, T>{

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Class<T> getEntityClass();

    public void create(T entity) {
        entityManager.persist(entity);
    }

    public Optional<T> findById(K id) {
        return Optional.ofNullable(entityManager.find(getEntityClass(), id));
    }

    public List<T> findAll() {
        CriteriaQuery<T>  query = entityManager.getCriteriaBuilder().createQuery(getEntityClass());
        query.from(getEntityClass());
        return entityManager.createQuery(query).getResultList();
    }

    public void update(T updatedEntity) {
        entityManager.merge(updatedEntity);
    }

    public void deleteById(K id) {
        findById(id).ifPresent(el -> entityManager.remove(el));
    }
}
