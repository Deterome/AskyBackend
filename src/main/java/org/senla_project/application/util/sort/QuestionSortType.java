package org.senla_project.application.util.sort;

import lombok.Getter;

public enum QuestionSortType {
    CREATE_TIME("CreateTime", "createTime"),
    INTEREST("Interest", "interesting");
//    ANSWERS_COUNT("AnswersCount", "count(answers)");

    private final String sortTypeName;
    @Getter
    private final String sortingFieldName;

    QuestionSortType(String sortTypeName, String sortingFieldName) {
        this.sortTypeName = sortTypeName;
        this.sortingFieldName = sortingFieldName;
    }

    @Override
    public String toString() {
        return this.sortTypeName;
    }
}
