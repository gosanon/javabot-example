package com.gosanon.javabot_example.main;

import com.gosanon.javabot_example.transports.ITransport;
import com.gosanon.javabot_example.transports.TgTransport;

public class Main {
    public static void main(String[] args) {
        String TOKEN = System.getenv("JAVABOT_TOKEN_TG");
        System.out.println(TOKEN);


        ITransport tgBot = new TgTransport(TOKEN);

        System.out.println("PROGRAM STARTED!");

        tgBot
            .addCommandHandler("/start", ctx -> ctx.reply("Чьих невольница ты идей?"))
            .addCommandHandler("/help", ctx -> ctx.reply("Ну чьих?"))

            .addContextHandler("Copy message if not answered by commands",
                ctx -> !ctx.isAlreadyReplied()
                    ? ctx.reply(ctx.newMessage.getMessageText())
                    : ctx
            )

            .startBot();
    }
}
