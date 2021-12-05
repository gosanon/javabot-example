package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.scenario.context.EventContext;

import java.util.regex.Pattern;

public class CommonHandlers {
    static ContextHandler notAnsweredThenCopy() {
        return ctx -> ctx
            .notYetReplied()
            ? ctx.reply(ctx.newMessage.getMessageText())
            : ctx;
    }

    static ContextHandler reply(String text) {
        return ctx -> ctx.reply(text);
    }

    static ContextHandler replyAndSetState(String text, String stateName) {
        return ctx -> ctx.reply(text).setState(stateName);
    }
}
