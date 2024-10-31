package org.senla_project.application.util.enums;

public enum DefaultRole {

    USER("user"),
    ADMIN("admin");

    final String roleName;

    DefaultRole(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }

}
