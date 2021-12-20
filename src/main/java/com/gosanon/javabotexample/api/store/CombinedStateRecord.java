package com.gosanon.javabotexample.api.store;

public class CombinedStateRecord<TUserData> {
    private final String stateName;
    private final TUserData userData;

    public CombinedStateRecord(String stateName, TUserData userData) {
        this.stateName = stateName;
        this.userData = userData;
    }

    public String getState() {
        return stateName;
    }

    public TUserData getUserData() {
        return userData;
    }
}
