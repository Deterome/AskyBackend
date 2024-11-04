package org.senla_project.application.util.sort;

import lombok.Getter;

public enum CollabSortType {
    COLLAB_NAME("CollabName", "collabName"),
    CREATE_DATE("CreateDate", "createDate");

    private final String sortTypeName;
    @Getter
    private final String sortingFieldName;

    CollabSortType(String sortTypeName, String sortingFieldName) {
        this.sortTypeName = sortTypeName;
        this.sortingFieldName = sortingFieldName;
    }

    @Override
    public String toString() {
        return this.sortTypeName;
    }
}
