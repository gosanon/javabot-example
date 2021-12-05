package com.gosanon.javabotexample.api.store;

public interface IStore {
    String getRecord(String id);
    void resetRecord(String id);
    void updateRecord(String id, String newValue);
    void deleteRecord(String id);
}
