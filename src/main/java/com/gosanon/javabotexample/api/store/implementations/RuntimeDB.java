package com.gosanon.javabotexample.api.store.implementations;

import com.gosanon.javabotexample.api.store.IUserStateStore;

import java.util.HashMap;

public class RuntimeDB implements IUserStateStore {
    private final String DEFAULT_VALUE;
    private final HashMap<String, String> db = new HashMap<>();

    public RuntimeDB(String defaultValue) {
        this.DEFAULT_VALUE = defaultValue;
    }

    public String getUserState(String userId) {
        if (db.containsKey(userId)) {
            return db.get(userId);
        }

        resetUserState(userId);
        return db.get(userId);
    }

    public void resetUserState(String id) {
        db.put(id, DEFAULT_VALUE);
    }

    public void updateUserState(String userId, String stateName) {
        db.put(userId, stateName);
    }

    public void deleteUserStateData(String userId) {
        db.remove(userId);
    }
}

