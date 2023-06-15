package com.krystiansledz.booktable;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, String> createErrorMap(String[] fields, String[] messages) {
        Map<String, String> errorMap = new HashMap<>();

        // Assuming fields and messages arrays are of same length
        for (int i = 0; i < fields.length; i++) {
            errorMap.put(fields[i], messages[i]);
        }

        return errorMap;
    }

}
