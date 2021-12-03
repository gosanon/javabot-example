package com.gosanon.javabotexample.api.store.implementations;

import com.gosanon.javabotexample.api.store.IStore;

import java.util.HashMap;

public class RuntimeDB implements IStore {
    private final String DEFAULT_VALUE;
    private final HashMap<String, String> db = new HashMap<>();

    public RuntimeDB(String defaultStateName) {
        this.DEFAULT_VALUE = defaultStateName;
    }

    public String getRecord(String id) {
        if (db.containsKey(id)) {
            return db.get(id);
        }

        resetRecord(id);
        return db.get(id);
    }

    public void resetRecord(String id) {
        db.put(id, DEFAULT_VALUE);
    }

    public void updateRecord(String id, String newValue) {
        db.put(id, newValue);
    }

    public void deleteRecord(String id) {
        db.remove(id);
    }
}

