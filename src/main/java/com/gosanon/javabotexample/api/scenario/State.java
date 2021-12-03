package com.gosanon.javabotexample.api.scenario;

import com.gosanon.javabotexample.api.scenario.context.ContextHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class State {
    String name;

    private final Map<String, ContextHandler> handlers = new LinkedHashMap<>();

    public State(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    public State addContextHandler(String handlerId, ContextHandler handler) {
        handlers.put(handlerId, handler);
        return this;
    }

    public State addCommandHandler(String commandText, ContextHandler handler) {
        return addContextHandler(commandText, ctx -> {
            if (ctx.newMessage.getMessageText().equals(commandText)) {
                return handler.apply(ctx);
            }

            return ctx;
        });
    }

    public ContextHandler buildFinalHandler() {
        ContextHandler complexHandler = ctx -> ctx;

        for (String handlerName: handlers.keySet()) {
            complexHandler = handlers.get(handlerName).compose(complexHandler)::apply;
        }

        return complexHandler;
    }
}
