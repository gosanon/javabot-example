package com.gosanon.javabotexample.api.store;

public interface IUserStore <TUserObject> extends IUserStateManager {
    TUserObject getUserData(String userId);
    void resetUserData(String userId);
    TUserObject updateUserData(String userId, TUserObject userData);
}
