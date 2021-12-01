package com.gosanon.javabotexample.main;

import java.util.HashMap;

public class RuntimeDB {
    private static String DEFAULT_VALUE = "";
    private static HashMap<String, String> db = new HashMap<>();

    public static String getById(String id) {
        return db.get(id);
    }

    public static void resetRecord(String id) {
        db.put(id, DEFAULT_VALUE);
    }

    public static void updateRecord(String id, String newNumber) {
        db.put(id, newNumber);
    }

    public static void deleteRecord(String id) {
        db.remove(id);
    }
}

