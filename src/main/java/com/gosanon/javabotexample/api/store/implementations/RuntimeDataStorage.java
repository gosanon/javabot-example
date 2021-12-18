package com.gosanon.javabotexample.api.store.implementations;

import com.gosanon.javabotexample.api.store.IUserDataStorage;

import java.util.HashMap;

public class RuntimeDataStorage<TUserRecord> implements IUserDataStorage<TUserRecord> {
    TUserRecord DEFAULT_VALUE;
    private final HashMap<String, TUserRecord> db = new HashMap<>();

    RuntimeDataStorage(TUserRecord defaultUserRecord) {
        this.DEFAULT_VALUE = defaultUserRecord;
    }

    @Override
    public TUserRecord getUserData(String userId) {
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
    public void updateUserData(String userId, TUserRecord userData) {
        db.put(userId, userData);
    }
}
