package com.gosanon.javabotexample.api.transports;

import com.gosanon.javabotexample.api.transports.context.ContextHandler;
import com.gosanon.javabotexample.api.transports.context.EventContext;

public interface ITransport {
    <T> EventContext toEventContext(T BaseContext);

    ITransport addContextHandler(String handlerId, ContextHandler handler);
    ITransport addCommandHandler(String commandText, ContextHandler handler);

    ITransport startBot();

    void sendMessage(String targetId, String messageText);
}
