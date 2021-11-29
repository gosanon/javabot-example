package com.gosanon.javabotexample.api.transports.context;

import com.gosanon.javabotexample.api.transports.ITransport;

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

    public boolean notYetReplied() {
        return !alreadyReplied;
    }
}
