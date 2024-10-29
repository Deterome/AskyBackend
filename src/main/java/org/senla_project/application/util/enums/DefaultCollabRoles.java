package org.senla_project.application.util.enums;

public enum DefaultCollabRoles {

    CREATOR("creator"),
    PARTICIPANT("participant");

    final String collabRoleName;

    DefaultCollabRoles(String roleName) {
        this.collabRoleName = roleName;
    }

    @Override
    public String toString() {
        return collabRoleName;
    }

}
