package org.senla_project.application.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.senla_project.application.entity.*;
import org.senla_project.application.repository.CustomizedUserCollaborationCollabRoleRepository;

public class CustomizedUserCollaborationCollabRoleRepositoryImpl implements CustomizedUserCollaborationCollabRoleRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void deleteByUsernameAndCollabNameAndCollabRoleName(String username, String collabName, String collabRoleName) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<UserCollaborationCollabRole> criteriaDelete = builder.createCriteriaDelete(UserCollaborationCollabRole.class);

        Root<UserCollaborationCollabRole> root = criteriaDelete.from(UserCollaborationCollabRole.class);
        Join<UserCollaborationCollabRole, User> userJoin = root.join(UserCollaborationCollabRole_.user, JoinType.LEFT);
        Join<UserCollaborationCollabRole, Collaboration> collabJoin = root.join(UserCollaborationCollabRole_.collab, JoinType.LEFT);
        Join<UserCollaborationCollabRole, CollabRole> collabRoleJoin = root.join(UserCollaborationCollabRole_.collabRole, JoinType.LEFT);

        Predicate equalsUsername = builder.equal(userJoin.get(User_.username), username);
        Predicate equalsCollabName = builder.equal(collabJoin.get(Collaboration_.collabName), collabName);
        Predicate equalsCollabRoleName = builder.equal(collabRoleJoin.get(CollabRole_.collabRoleName), collabRoleName);

        criteriaDelete.where(builder.and(equalsUsername, equalsCollabName, equalsCollabRoleName));

        em.createQuery(criteriaDelete).executeUpdate();
    }
}
