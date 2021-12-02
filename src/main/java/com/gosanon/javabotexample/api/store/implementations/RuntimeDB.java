package com.gosanon.javabotexample.api.store.implementations;

import com.gosanon.javabotexample.api.store.IStore;

import java.util.HashMap;

public class RuntimeDB implements IStore {
    private String DEFAULT_VALUE = "";
    private HashMap<String, String> db = new HashMap<>();

    public String getRecord(String id) {
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

