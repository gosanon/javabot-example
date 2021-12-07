package com.gosanon.javabotexample.api.store;

public interface IUserStore <TUserObject> extends IUserStateStore {
    void resetUserData(String userId);
    TUserObject getUserData(String userId);
    TUserObject setUserData(String userId, TUserObject userData);
}
