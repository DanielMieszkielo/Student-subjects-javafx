package com.library;

import java.util.ArrayList;
import java.util.HashMap;

public class Helpers {
    public static String prepare_query_params(HashMap<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }

        String query = " WHERE  ";
        ArrayList<String> keyValues = new ArrayList<>();
        for (String param : params.keySet()) {
            keyValues.add(param + "='" + params.get(param) + "'");
        }
        return query + String.join(" AND ", keyValues);
    }
}
