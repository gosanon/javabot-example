package com.gosanon.javabot_example.transports;

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

    private final Map<String, Function<EventContext, EventContext>> handlers = new LinkedHashMap<>();
    private boolean isBotStarted = false;

    public TgTransport(String TOKEN) {
        bot = new TelegramBot(TOKEN);
    }

    public ITransport addContextHandler(String handlerId, Function<EventContext, EventContext> handler) {
        if (isBotStarted)
            throw new RuntimeException("Adding handlers after bot start is not allowed");

        handlers.put(handlerId, handler);
        return this;
    }

    public ITransport removeContextHandler(String handlerId) {
        if (isBotStarted)
            throw new RuntimeException("Removing handlers after bot start is not allowed");

        return this;
    }

    public ITransport addCommandHandler(String commandText, Function<EventContext, EventContext> handler) {
        return addContextHandler(commandText, ctx -> {
            //System.out.println("!!!");
            //System.out.println(ctx.newMessage.getMessageText().equals(commandText));

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
        Function<EventContext, EventContext> complexHandler = ctx -> ctx;
        for (String handlerName: handlers.keySet()) {
            System.out.println(handlerName);
            complexHandler = handlers.get(handlerName).compose(complexHandler);
        }
        Function<EventContext, EventContext> finalHandler = complexHandler;

        // INIT BOT
        System.out.println("BOT STARTED!");
        bot.setUpdatesListener(updateList -> {
            updateList.forEach(update -> finalHandler.apply(new EventContext(this, update)));
            //updateList.forEach(System.out::println);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        return this;
    }

    public void sendMessage(String targetId, String messageText) {
        bot.execute(new SendMessage(parseInt(targetId), messageText));
    }
}
