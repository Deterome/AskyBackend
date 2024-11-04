package org.senla_project.application.util.convertor;

import org.senla_project.application.util.sort.UserSortType;

public class StringToUserSortTypeConvertor extends StringToEnumConvertor<UserSortType> {
    public StringToUserSortTypeConvertor() {
        super(UserSortType.class);
    }
}
