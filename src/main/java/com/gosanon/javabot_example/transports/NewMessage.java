package com.gosanon.javabot_example.transports;

public class NewMessage {
    private String messageText;
    private String senderId;

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
