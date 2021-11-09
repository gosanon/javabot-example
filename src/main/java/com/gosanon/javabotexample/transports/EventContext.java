package com.gosanon.javabotexample.transports;

import com.pengrad.telegrambot.model.Update;

public class EventContext {
    private ITransport transport;
    public NewMessage newMessage;

    private boolean alreadyReplied;

    public <T> EventContext(ITransport _transport, T ctx) {
        if (ctx instanceof com.pengrad.telegrambot.model.Update) {
            var updateObject = (Update) ctx;
            String newMessageText = updateObject.message().text();
            String newMessageSenderId = updateObject.message().from().id().toString();

            newMessage = new NewMessage(newMessageText, newMessageSenderId);
            transport = _transport;
        }
    }

    public EventContext reply(String replyMessage) {
        String target = newMessage.getSenderId();
        transport.sendMessage(target, replyMessage);
        if (!alreadyReplied)
            alreadyReplied = true;
        return this;
    }

    public boolean isAlreadyReplied() {
        return alreadyReplied;
    }
}
