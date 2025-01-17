package org.senla_project.application.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.senla_project.application.entity.*;
import org.senla_project.application.repository.CustomizedCollabRoleRepository;

import java.util.List;

public class CustomizedCollabRoleRepositoryImpl implements CustomizedCollabRoleRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CollabRole> findByUsernameAndCollabName(String username, String collabName) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CollabRole> query = builder.createQuery(CollabRole.class);

        Root<CollabRole> root = query.from(CollabRole.class);
        Join<CollabRole, UserCollaborationCollabRole> userCollaborationCollabRoleJoin = root.join(CollabRole_.usersWithRoles, JoinType.LEFT);
        Join<UserCollaborationCollabRole, User> userJoin = userCollaborationCollabRoleJoin.join(UserCollaborationCollabRole_.user, JoinType.LEFT);
        Join<UserCollaborationCollabRole, Collaboration> collabJoin = userCollaborationCollabRoleJoin.join(UserCollaborationCollabRole_.collab, JoinType.LEFT);

        Predicate equalsUsername = builder.equal(userJoin.get(User_.username), username);
        Predicate equalsCollabName = builder.equal(collabJoin.get(Collaboration_.collabName), collabName);

        query.select(root).where(builder.and(equalsUsername, equalsCollabName));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CollabRole> findByCollabName(String collabName) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CollabRole> query = builder.createQuery(CollabRole.class);

        Root<CollabRole> root = query.from(CollabRole.class);
        Join<CollabRole, Collaboration> collabJoin = root.join(CollabRole_.collabs, JoinType.LEFT);

        Predicate equalsCollabName = builder.equal(collabJoin.get(Collaboration_.collabName), collabName);

        query.select(root).where(equalsCollabName);

        return em.createQuery(query).getResultList();
    }
}
