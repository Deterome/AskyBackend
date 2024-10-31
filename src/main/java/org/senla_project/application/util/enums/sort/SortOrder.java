package org.senla_project.application.util.enums.sort;

public enum SortOrder {
    ASCENDING("Ascending"),
    DESCENDING("Descending");

    final String sortOrderName;

    SortOrder(String sortTypeName) {
        this.sortOrderName = sortTypeName;
    }

    @Override
    public String toString() {
        return this.sortOrderName;
    }
}
