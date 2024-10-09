package org.senla_project.application.repository.impl;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.senla_project.application.entity.*;
import org.senla_project.application.repository.AbstractDao;
import org.senla_project.application.repository.RoleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryImpl extends AbstractDao<UUID, Role> implements RoleRepository {

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    public Optional<Role> findRoleByName(String roleName) {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role AS r WHERE r.roleName = :roleName", Role.class);
        query.setParameter("roleName", roleName);

        List<Role> results = query.getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }

}
