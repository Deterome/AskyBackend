package org.senla_project.application.util.data;

public enum DefaultRole {

    USER("user"),
    ADMIN("admin");

    private final String roleName;

    DefaultRole(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }

}
