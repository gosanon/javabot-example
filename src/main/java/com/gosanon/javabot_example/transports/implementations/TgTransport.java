package com.gosanon.javabot_example.transports.implementations;

import com.gosanon.javabot_example.transports.ContextHandler;
import com.gosanon.javabot_example.transports.EventContext;
import com.gosanon.javabot_example.transports.ITransport;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class TgTransport implements ITransport {
    // На рамке монитора виднелись красные капли вишнёвого сока. Джава проиграла сегодня.
    // Но мы оба знали. Хоть бой и закончен, но это лишь начало войны. Жестокой. Но неизбежной.
    TelegramBot bot;

    private final Map<String, ContextHandler> handlers = new LinkedHashMap<>();
    private boolean isBotStarted = false;

    public TgTransport(String TOKEN) {
        bot = new TelegramBot(TOKEN);
    }

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

    public ITransport startBot() {
        if (isBotStarted)
            throw new RuntimeException("Calling startBot() after bot start is not allowed");

        isBotStarted = true;

        // COMPOSE HANDLERS
        ContextHandler complexHandler = ctx -> ctx;
        for (String handlerName: handlers.keySet()) {
            complexHandler = (ContextHandler) handlers.get(handlerName).compose(complexHandler);
        }
        var finalHandler = complexHandler;

        // INIT BOT
        System.out.println("BOT STARTED!");
        bot.setUpdatesListener(updateList -> {
            updateList.forEach(update -> finalHandler.apply(new EventContext(this, update)));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        return this;
    }

    public void sendMessage(String targetId, String messageText) {
        bot.execute(new SendMessage(parseInt(targetId), messageText));
    }
}
