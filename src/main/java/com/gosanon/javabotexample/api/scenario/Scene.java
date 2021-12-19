package com.gosanon.javabotexample.api.scenario;

import com.gosanon.javabotexample.api.scenario.context.ContextHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class Scene {
    String name;

    private final Map<String, ContextHandler> handlers = new LinkedHashMap<>();
    private int lastHandlerId = 0;

    public Scene(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    public Scene addContextHandler(ContextHandler handler) {
        var handlerId = String.format("JAVABOT_HANDLER_%d", lastHandlerId);
        lastHandlerId++;
        handlers.put(handlerId, handler);
        return this;
    }

    public Scene addCommandHandler(String commandText, ContextHandler handler) {
        handlers.put(commandText, ctx -> {
            if (ctx.newMessage.getMessageText().equals(commandText)) {
                return handler.apply(ctx);
            }

            return ctx;
        });

        return this;
    }

    ContextHandler buildFinalHandler() {
        ContextHandler complexHandler = ctx -> ctx;

        for (String handlerName: handlers.keySet()) {
            complexHandler = handlers.get(handlerName).compose(complexHandler)::apply;
        }

        return complexHandler;
    }
}
