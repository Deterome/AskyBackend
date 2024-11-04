package org.senla_project.application.util.data;

public enum DefaultCollabRole {

    CREATOR("creator"),
    PARTICIPANT("participant");

    final String collabRoleName;

    DefaultCollabRole(String roleName) {
        this.collabRoleName = roleName;
    }

    @Override
    public String toString() {
        return collabRoleName;
    }

}
