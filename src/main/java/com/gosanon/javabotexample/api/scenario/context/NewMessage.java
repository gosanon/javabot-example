package com.gosanon.javabotexample.api.scenario.context;

public class NewMessage {
    private final String messageText;
    private final String senderId;

    NewMessage(String _messageText, String _senderId) {
        messageText = _messageText;
        senderId = _senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSenderId() {
        return senderId;
    }
}
