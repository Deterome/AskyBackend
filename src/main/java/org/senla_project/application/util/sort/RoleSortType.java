package org.senla_project.application.util.sort;

import lombok.Getter;

public enum RoleSortType {
    ROLE_NAME("RoleName", "roleName");

    private final String sortTypeName;
    @Getter
    private final String sortingFieldName;

    RoleSortType(String sortTypeName, String sortingFieldName) {
        this.sortTypeName = sortTypeName;
        this.sortingFieldName = sortingFieldName;
    }

    @Override
    public String toString() {
        return this.sortTypeName;
    }
}
