package com.gosanon.javabotexample.api.scenario.context;

import com.gosanon.javabotexample.api.store.IUserStateManager;
import com.gosanon.javabotexample.api.transports.ITransport;

public class EventContext {
    private final ITransport transport;
    private IUserStateManager userStateManager;
    public NewMessage newMessage;

    private boolean userStateManagerIsSet = false;
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
    public void setUserStateManager(IUserStateManager userStateManager) {
        if (userStateManagerIsSet) {
            throw new RuntimeException("User state manager already set before!");
        }

        this.userStateManager = userStateManager;
        userStateManagerIsSet = true;
    }

    public EventContext toScene(String sceneName) {
        var userId = this.newMessage.getSenderId();
        userStateManager.updateUserState(userId, sceneName);
        return this;
    }

    public EventContext sendPhoto(String photoUrl) {
        transport.sendPhoto(newMessage.getSenderId(), photoUrl);
        return this;
    }
}
