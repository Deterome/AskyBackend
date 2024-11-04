package org.senla_project.application.util.sort;

import lombok.Getter;

public enum ProfileSortType {
    USERNAME("Username", "user.username"),
    BIRTHDAY("Birthday", "birthday"),
    RATING("Rating", "rating");

    private final String sortTypeName;
    @Getter
    private final String sortingFieldName;

    ProfileSortType(String sortTypeName, String sortingFieldName) {
        this.sortTypeName = sortTypeName;
        this.sortingFieldName = sortingFieldName;
    }

    @Override
    public String toString() {
        return this.sortTypeName;
    }
}
