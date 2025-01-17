package org.senla_project.application.util.convertor;

import org.senla_project.application.util.sort.SortOrder;

public class StringToSortOrderConvertor extends StringToEnumConvertor<SortOrder> {
    public StringToSortOrderConvertor() {
        super(SortOrder.class);
    }
}
