package com.gosanon.javabot_example.transports;

import java.util.function.Function;

public interface ITransport {
    //void sendMessage (int targetChatId, String messageText); // targetChatId breaks the abstraction
    ITransport addContextHandler(String handlerId, Function<EventContext, EventContext> handler);
    ITransport removeContextHandler(String handlerId);

    ITransport addCommandHandler(String commandText, Function<EventContext, EventContext> handler);
    ITransport removeCommandHandler(String commandText);

    ITransport startBot();

    void sendMessage(String targetId, String messageText);
}
