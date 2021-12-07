package com.gosanon.javabotexample.transports.implementations;

import com.gosanon.javabotexample.api.transports.CommonTransport;
import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.scenario.context.EventContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestStringTransport extends CommonTransport {

    private String[] userMessages;

    public List<String> getReplies() {
        return replies;
    }

    private List<String> replies = new ArrayList<>();

    public String senderId = "hooba-booba";

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

    public void clearChatHistory(){
        userMessages = null;
        replies = new ArrayList<>();
    }

    public void expectBehaviour(String[] userMessages, String[] expectedReplies){
        this.processMessages(userMessages);
        assertArrayEquals(expectedReplies, this.getReplies().toArray());
        clearChatHistory();
    }

    public void processMessages(String[] userMessages){
        this.sendUserMessages(userMessages);

        for (var message : this.userMessages) {
            this.boundScenarios.forEach(scenario -> scenario.complexScenarioHandler.apply(this.toEventContext(message)));
        }
    }
}

