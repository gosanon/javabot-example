package com.gosanon.javabotexample.api.transports;

import com.gosanon.javabotexample.api.scenario.Scenario;
import com.gosanon.javabotexample.api.scenario.context.EventContext;

public interface ITransport {
    <T> EventContext toEventContext(T BaseContext);
    void bindScenarioHandler(Scenario scenario);
    void sendMessage(String targetId, String messageText);
    void sendPhoto(String targetId, String photoURL);
}
