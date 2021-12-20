package com.gosanon.javabotexample.api.store.implementations;

import com.gosanon.javabotexample.api.store.CombinedStateRecord;
import com.gosanon.javabotexample.api.store.IStore;

import java.util.HashMap;

public class RuntimeStore<TUserData> implements IStore<TUserData> {
    private final CombinedStateRecord<TUserData> DEFAULT_VALUE;
    protected final HashMap<String, CombinedStateRecord<TUserData>> db = new HashMap<>();

    public RuntimeStore(String defaultState, TUserData defaultUserRecord) {
        DEFAULT_VALUE = new CombinedStateRecord<>(defaultState, defaultUserRecord);
    }

    @Override
    public TUserData getUserData(String userId) {
        if (db.containsKey(userId)) {
            return db.get(userId).getUserData();
        }

        resetUserData(userId);
        return db.get(userId).getUserData();
    }

    @Override
    public void resetUserData(String userId) {
        if (!db.containsKey(userId)) {
            db.put(userId, DEFAULT_VALUE);
        }

        var userState = getUserState(userId);
        db.put(userId, new CombinedStateRecord<>(userState, DEFAULT_VALUE.getUserData()));
    }

    @Override
    public void updateUserData(String userId, TUserData userData) {
        var userState = getUserState(userId);
        db.put(userId, new CombinedStateRecord<>(userState, userData));
    }

    @Override
    public String getUserState(String userId) {
        if (db.containsKey(userId)) {
            return db.get(userId).getState();
        }

        resetUserState(userId);
        return db.get(userId).getState();
    }

    @Override
    public void resetUserState(String userId) {
        if (!db.containsKey(userId)) {
            db.put(userId, DEFAULT_VALUE);
        }

        var userData = getUserData(userId);
        db.put(userId, new CombinedStateRecord<>(DEFAULT_VALUE.getState(), userData));
    }

    @Override
    public void updateUserState(String userId, String stateName) {
        var userData = getUserData(userId);
        db.put(userId, new CombinedStateRecord<>(stateName, userData));
    }

    CombinedStateRecord<TUserData> getCombinedUserRecord(String userId) {
        return db.get(userId);
    }

    void resetCombinedUserRecord(String userId) {
        db.put(userId, DEFAULT_VALUE);
    }

    void updateCombinedUserRecord(String userId, CombinedStateRecord<TUserData> newValue) {
        db.put(userId, newValue);
    }
}

