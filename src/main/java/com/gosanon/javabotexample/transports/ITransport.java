package com.gosanon.javabotexample.transports;

public interface ITransport {
    ITransport addContextHandler(String handlerId, ContextHandler handler);
    ITransport removeContextHandler(String handlerId);

    ITransport addCommandHandler(String commandText, ContextHandler handler);
    ITransport removeCommandHandler(String commandText);

    ITransport startBot();

    void sendMessage(String targetId, String messageText);
}
