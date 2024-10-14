package org.senla_project.application.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<K, T> implements DefaultDao<K, T> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Class<T> getEntityClass();

    public T create(T entity) {
        entityManager.persist(entity);
        entityManager.flush();

        return entity;
    }

    public Optional<T> findById(K id) {
        return Optional.ofNullable(entityManager.find(getEntityClass(), id));
    }

    public List<T> findAll(int pageNumber) {
        int pageSize = 15;
        CriteriaQuery<Long> countQuery = entityManager.getCriteriaBuilder()
                .createQuery(Long.class);
        countQuery.select(entityManager.getCriteriaBuilder()
                .count(countQuery.from(getEntityClass())));
        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();

        CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(getEntityClass());
        Root<T> from = query.from(getEntityClass());
        CriteriaQuery<T> select = query.select(from);

        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public T update(T updatedEntity) {
        return entityManager.merge(updatedEntity);
    }

    public void deleteById(K id) {
        findById(id).ifPresent(el -> entityManager.remove(el));
    }
}
