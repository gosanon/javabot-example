package com.gosanon.javabotexample.api.store;

public interface IUserStateManager {
    String getUserState(String userId);
    void resetUserState(String userId);
    void updateUserState(String userId, String stateName);
}
