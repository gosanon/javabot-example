package com.gosanon.javabotexample.api.scenario.context;

import com.gosanon.javabotexample.api.store.IStore;
import com.gosanon.javabotexample.api.transports.ITransport;

public class EventContext {
    private final ITransport transport;
    private IStore store;
    public NewMessage newMessage;

    private boolean alreadyReplied;

    public EventContext(ITransport transport, String newMessageText, String newMessageSenderId) {
        this.newMessage = new NewMessage(newMessageText, newMessageSenderId);
        this.transport = transport;
    }

    public boolean notYetReplied() {
        return !alreadyReplied;
    }

    public EventContext reply(String replyMessage) {
        String target = newMessage.getSenderId();
        transport.sendMessage(target, replyMessage);
        if (!alreadyReplied)
            alreadyReplied = true;
        return this;
    }

    // I shall think where I should call this.
    public void setStore(IStore store) {
        this.store = store;
    }

    public EventContext setState(String newStateName) {
        var userId = this.newMessage.getSenderId();
        store.updateRecord(userId, newStateName);
        return this;
    }

    public EventContext sendPhoto(String photoUrl) {
        transport.sendPhoto(newMessage.getSenderId(), photoUrl);
        return this;
    }
}
