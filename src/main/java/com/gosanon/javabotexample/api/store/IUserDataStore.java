package com.gosanon.javabotexample.api.store;

public interface IUserDataStore<TUserObject> {
    TUserObject getUserData(String userId);
    void resetUserData(String userId);
    TUserObject updateUserData(String userId, TUserObject userData);
}
