package com.gosanon.javabotexample.api.store.implementations;

import com.gosanon.javabotexample.api.store.IUserStateManager;

import java.util.HashMap;

public class RuntimeStateManager implements IUserStateManager {
    private final String DEFAULT_VALUE;
    private final HashMap<String, String> db = new HashMap<>();

    public RuntimeStateManager(String defaultValue) {
        this.DEFAULT_VALUE = defaultValue;
    }

    @Override
    public String getUserState(String userId) {
        if (db.containsKey(userId)) {
            return db.get(userId);
        }

        resetUserState(userId);
        return db.get(userId);
    }

    @Override
    public void resetUserState(String userId) {
        db.put(userId, DEFAULT_VALUE);
    }

    @Override
    public void updateUserState(String userId, String stateName) {
        db.put(userId, stateName);
    }
}

