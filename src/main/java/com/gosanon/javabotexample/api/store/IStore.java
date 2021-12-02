package com.gosanon.javabotexample.api.store;

import org.apache.commons.lang3.NotImplementedException;

public interface IStore {
    String getById(String id);
    void resetRecord(String id);
    void updateRecord(String id, String newValue);
    void deleteRecord(String id);
}
