package com.gosanon.javabotexample.api.store;

public interface IUserStateStore {
    String getUserState(String userId);
    void resetUserState(String userId);
    void updateUserState(String userId, String stateName);
    void deleteUserStateData(String userId);
}
