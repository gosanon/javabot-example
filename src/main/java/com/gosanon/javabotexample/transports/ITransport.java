package com.gosanon.javabotexample.transports;

public interface ITransport {
    <T> EventContext toEventContext(T BaseContext);

    ITransport addContextHandler(String handlerId, ContextHandler handler);
    ITransport addCommandHandler(String commandText, ContextHandler handler);

    ITransport startBot();

    void sendMessage(String targetId, String messageText);
}
