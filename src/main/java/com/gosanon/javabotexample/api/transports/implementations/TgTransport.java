package com.gosanon.javabotexample.api.transports.implementations;

import com.gosanon.javabotexample.api.scenario.context.EventContext;
import com.gosanon.javabotexample.api.transports.CommonTransport;
import com.gosanon.javabotexample.api.scenario.context.ContextHandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;

import static java.lang.Integer.parseInt;

public class TgTransport extends CommonTransport {
    // На рамке монитора виднелись красные капли вишнёвого сока. Джава проиграла сегодня.
    // Но мы оба знали. Хоть бой и закончен, но это лишь начало войны. Жестокой. Но неизбежной.
    TelegramBot bot;

    public TgTransport(String TOKEN) {
        bot = new TelegramBot(TOKEN);

        System.out.println("BOT STARTED!");
        bot.setUpdatesListener(updateList -> {
            for (var scenario: boundScenarios) {
                updateList.forEach(update -> {
                    if (update.myChatMember() != null) {
                        // Means Update type is "ChatMemberUpdated", we ignore this
                        return;
                    }
                    scenario.complexScenarioHandler.apply(this.toEventContext(update));
                });
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public <T> EventContext toEventContext(T BaseContext) {
        var update = (Update) BaseContext;
        String newMessageText = update.message().text();
        String newMessageSenderId = update.message().from().id().toString();

        return new EventContext(this, newMessageText, newMessageSenderId);
    }

    public void sendMessage(String targetId, String messageText) {
        bot.execute(new SendMessage(parseInt(targetId), messageText));
    }
    public void sendPhoto(String targetId, String photoURL){
        bot.execute(new SendPhoto(parseInt(targetId), photoURL));
    }
}
