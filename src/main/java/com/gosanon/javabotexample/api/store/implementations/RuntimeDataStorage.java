package com.gosanon.javabotexample.api.store.implementations;

import com.gosanon.javabotexample.api.store.IUserDataStorage;

import java.util.HashMap;

public class RuntimeDataStorage<TUserData> implements IUserDataStorage<TUserData> {
    TUserData DEFAULT_VALUE;
    private final HashMap<String, TUserData> db = new HashMap<>();

    RuntimeDataStorage(TUserData defaultUserRecord) {
        this.DEFAULT_VALUE = defaultUserRecord;
    }

    @Override
    public TUserData getUserData(String userId) {
        if (db.containsKey(userId)) {
            return db.get(userId);
        }

        resetUserData(userId);
        return db.get(userId);
    }

    @Override
    public void resetUserData(String userId) {
        db.put(userId, DEFAULT_VALUE);
    }

    @Override
    public void updateUserData(String userId, TUserData userData) {
        db.put(userId, userData);
    }
}
