package com.gosanon.javabotexample.transports;

public interface ITransport {
    ITransport addContextHandler(String handlerId, ContextHandler handler);
    ITransport addCommandHandler(String commandText, ContextHandler handler);

    ITransport startBot();

    void sendMessage(String targetId, String messageText);
}
