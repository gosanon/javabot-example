package com.gosanon.javabotexample.api.store;

public interface IUserDataStorage<TUserRecord> {
    TUserRecord getUserData(String userId);
    void resetUserData(String userId);
    void updateUserData(String userId, TUserRecord userData);
}
