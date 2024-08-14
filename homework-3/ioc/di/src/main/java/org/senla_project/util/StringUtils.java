package org.senla_project.util;

final public class StringUtils {

    private StringUtils() {}

    static public String lowercaseFirst(String str) {
        if (str == null || str.isEmpty()) return str;

        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    static public String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
