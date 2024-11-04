package org.senla_project.application.util.sort;

import lombok.Getter;

public enum UserSortType {
    USERNAME("Username", "username");

    private final String sortTypeName;
    @Getter
    private final String sortingFieldName;

    UserSortType(String sortTypeName, String sortingFieldName) {
        this.sortTypeName = sortTypeName;
        this.sortingFieldName = sortingFieldName;
    }

    @Override
    public String toString() {
        return this.sortTypeName;
    }
}
