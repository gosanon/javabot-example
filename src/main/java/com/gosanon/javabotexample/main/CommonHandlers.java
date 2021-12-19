package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.context.ContextHandler;

public class CommonHandlers {

    public static ContextHandler notAnsweredThenCopy() {
        return ctx -> ctx
            .notYetReplied()
            ? ctx.reply(ctx.newMessage.getMessageText())
            : ctx;
    }

    public static ContextHandler reply(String text) {
        return ctx -> ctx.reply(text);
    }

    public static ContextHandler replyAndSetState(String text, String stateName) {
        return ctx -> ctx.reply(text).toScene(stateName);
    }
}
