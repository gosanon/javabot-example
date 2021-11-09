package com.gosanon.javabotexample.transports;

import com.pengrad.telegrambot.model.Update;

public class EventContext {
    private ITransport transport;
    public NewMessage newMessage;

    private boolean alreadyReplied;

    public EventContext(ITransport transport, String newMessageText, String newMessageSenderId) {
        this.newMessage = new NewMessage(newMessageText, newMessageSenderId);
        this.transport = transport;
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
