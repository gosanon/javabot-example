package com.gosanon.javabotexample.transports;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class CommonTransportTest {

    private final TestStringTransport simpleTestBot = (TestStringTransport) (new TestStringTransport())
        .addCommandHandler("/start", ctx -> ctx.reply("Чьих невольница ты идей?"))
        .addCommandHandler("/help", ctx -> ctx.reply("Ну чьих?"))

        .addContextHandler("Copy message if not answered by commands",
            ctx -> !ctx.isAlreadyReplied()
                ? ctx.reply(ctx.newMessage.getMessageText())
                : ctx
        )

        .startBot();

    @Test
    public void testWithoutUserMessages(){
        simpleTestBot.expectBehaviour(new String[]{}, new String[]{});
    }

    @Test
    public void testOnKnownCommands(){
        simpleTestBot.expectBehaviour(
            new String[]{"/start", "/help"},
            new String[]{"Чьих невольница ты идей?", "Ну чьих?"});
    }

    @Test
    public void testOnUnknownInput(){
        simpleTestBot.expectBehaviour(
            new String[]{"123", "ты кто такой?"},
            new String[]{"123", "ты кто такой?"});
    }

    @Test
    public void testDoubleStart(){
        try {
            simpleTestBot.startBot();
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("Calling startBot() after bot start is not allowed", e.getMessage());
        }
    }
}