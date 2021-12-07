package com.gosanon.javabotexample.api.transports;

import com.gosanon.javabotexample.api.scenario.StateScenario;
import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.scenario.context.EventContext;

public interface ITransport {
    <T> EventContext toEventContext(T BaseContext);
    void bindScenarioHandler(StateScenario scenario);
    void sendMessage(String targetId, String messageText);
    void sendPhoto(String targetId, String photoURL);
}
