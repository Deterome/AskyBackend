package org.senla_project.application.util.enums.sort;

public enum QuestionSortType {
    CREATE_TIME("CreateTime"),
    INTEREST("Interest"),
    ANSWERS_COUNT("AnswersCount");

    final String sortTypeName;

    QuestionSortType(String sortTypeName) {
        this.sortTypeName = sortTypeName;
    }

    @Override
    public String toString() {
        return this.sortTypeName;
    }

}
