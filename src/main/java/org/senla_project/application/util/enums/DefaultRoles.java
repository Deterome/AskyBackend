package org.senla_project.application.util.enums;

public enum DefaultRoles {

    USER("user"),
    ADMIN("admin");

    final String roleName;

    DefaultRoles(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }

}
