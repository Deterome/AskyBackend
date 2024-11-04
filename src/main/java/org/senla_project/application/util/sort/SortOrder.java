package org.senla_project.application.util.sort;

public enum SortOrder {
    ASCENDING("Ascending"),
    DESCENDING("Descending");

    private final String sortOrderName;

    SortOrder(String sortTypeName) {
        this.sortOrderName = sortTypeName;
    }

    @Override
    public String toString() {
        return this.sortOrderName;
    }
}
