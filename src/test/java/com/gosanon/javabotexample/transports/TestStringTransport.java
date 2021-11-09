package com.gosanon.javabotexample.transports;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestStringTransport extends CommonTransport {

    private String[] userMessages;

    public List<String> getReplies() {
        return replies;
    }

    private List<String> replies;
    private String senderId = "hooba-booba";


    @Override
    protected void initBot(ContextHandler finalHandler) {
        for (var message : userMessages) {
            finalHandler.apply(this.toEventContext(message));
        }
    }

    public void sendUserMessages(String[] userMessages){
        this.userMessages = userMessages;
    }

    @Override
    public <T> EventContext toEventContext(T BaseContext) {
        var message = BaseContext.toString();
        return new EventContext(this, message, senderId);
    }

    @Override
    public void sendMessage(String targetId, String messageText) {
        replies.add(messageText);
    }

    public void expectBehaviour(String[] userMessages, String[] expectedReplies){
        this.sendUserMessages(userMessages);
        assertArrayEquals(this.getReplies().toArray(), expectedReplies);
        clearChatHistory();
    }

    public void clearChatHistory(){
        userMessages = null;
        replies = null;
    }
}
