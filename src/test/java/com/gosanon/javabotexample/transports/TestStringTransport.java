package com.gosanon.javabotexample.transports;

import com.gosanon.javabotexample.api.transports.CommonTransport;
import com.gosanon.javabotexample.api.transports.context.ContextHandler;
import com.gosanon.javabotexample.api.transports.context.EventContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestStringTransport extends CommonTransport {

    private String[] userMessages;

    public List<String> getReplies() {
        return replies;
    }

    private List<String> replies = new ArrayList<String>();


    @Override
    protected void initBot(ContextHandler finalHandler) {
        // finalHandler has been made a field which means
        // TestStringTransport does not have to init at all
    }

    public void sendUserMessages(String[] userMessages){
        this.userMessages = userMessages;
    }

    @Override
    public <T> EventContext toEventContext(T BaseContext) {
        var message = BaseContext.toString();
        String senderId = "hooba-booba";
        return new EventContext(this, message, senderId);
    }

    @Override
    public void sendMessage(String targetId, String messageText) {
        replies.add(messageText);
    }

    public void expectBehaviour(String[] userMessages, String[] expectedReplies){
        this.sendUserMessages(userMessages);

        for (var message : this.userMessages) {
            this.finalHandler.apply(this.toEventContext(message));
        }

        assertArrayEquals(this.getReplies().toArray(), expectedReplies);
        clearChatHistory();
    }

    public void clearChatHistory(){
        userMessages = null;
        replies = new ArrayList<String>();
    }
}
