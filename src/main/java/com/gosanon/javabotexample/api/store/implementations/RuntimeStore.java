package com.gosanon.javabotexample.api.store.implementations;

import com.gosanon.javabotexample.api.store.IStore;

public class RuntimeStore<TUserRecord> implements IStore<TUserRecord> {
    RuntimeDataStorage<TUserRecord> runtimeDataStorage;
    RuntimeStateManager runtimeStateManager;

    public RuntimeStore(String defaultState, TUserRecord defaultUserRecord) {
        this.runtimeDataStorage = new RuntimeDataStorage<>(defaultUserRecord);
        this.runtimeStateManager = new RuntimeStateManager(defaultState);
    }

    @Override
    public String getUserState(String userId) {
        return runtimeStateManager.getUserState(userId);
    }

    @Override
    public void resetUserState(String userId) {
        runtimeStateManager.resetUserState(userId);
    }

    @Override
    public void updateUserState(String userId, String stateName) {
        runtimeStateManager.updateUserState(userId, stateName);
    }

    @Override
    public TUserRecord getUserData(String userId) {
        return runtimeDataStorage.getUserData(userId);
    }

    @Override
    public void resetUserData(String userId) {
        runtimeDataStorage.resetUserData(userId);
    }

    @Override
    public void updateUserData(String userId, TUserRecord userData) {
        runtimeDataStorage.updateUserData(userId, userData);
    }
}

