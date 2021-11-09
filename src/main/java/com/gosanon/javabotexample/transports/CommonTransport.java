package com.gosanon.javabotexample.transports;


import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CommonTransport implements ITransport {
    private final Map<String, ContextHandler> handlers = new LinkedHashMap<>();
    private boolean isBotStarted = false;

    public ITransport addContextHandler(String handlerId, ContextHandler handler) {
        if (isBotStarted)
            throw new RuntimeException("Adding handlers after bot start is not allowed");

        handlers.put(handlerId, handler);
        return this;
    }

    public ITransport removeContextHandler(String handlerId) {
        if (isBotStarted)
            throw new RuntimeException("Removing handlers after bot start is not allowed");

        // Not implemented, method is probably useless
        throw new RuntimeException("removeContextHandler is not implemented");

        //return this;
    }

    public ITransport addCommandHandler(String commandText, ContextHandler handler) {
        return addContextHandler(commandText, ctx -> {
            if (ctx.newMessage.getMessageText().equals(commandText)) {
                return handler.apply(ctx);
            }

            return ctx;
        });
    }

    public ITransport removeCommandHandler(String commandText) {
        return removeContextHandler(commandText);
    }

    abstract protected void initBot(ContextHandler finalHandler);

    public ITransport startBot() {
        if (isBotStarted)
            throw new RuntimeException("Calling startBot() after bot start is not allowed");

        isBotStarted = true;

        // COMPOSE HANDLERS
        ContextHandler complexHandler = ctx -> ctx;
        for (String handlerName: handlers.keySet()) {
            complexHandler = handlers.get(handlerName).compose(complexHandler)::apply;
        }
        var finalHandler = complexHandler;

        // INIT BOT
        initBot(finalHandler);

        return this;
    }

    public void sendMessage(String targetId, String messageText) {}
}
