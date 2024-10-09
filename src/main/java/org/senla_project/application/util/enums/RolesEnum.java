package org.senla_project.application.util.enums;

public enum RolesEnum {

    USER("user"),
    ADMIN("admin");

    final String roleName;

    RolesEnum(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }

}
