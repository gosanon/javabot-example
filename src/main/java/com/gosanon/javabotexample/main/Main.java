package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.transports.ITransport;
import com.gosanon.javabotexample.transports.implementations.TgTransport;

public class Main {
    public static void main(String[] args) {
/*        String TOKEN = System.getenv("JAVABOT_TOKEN_TG");
        System.out.println(TOKEN);
        String startMessage = "Пишите /quiz *число вопросов* для игры в викторину. Справка по команде /help";
        String helpMessage = "Бот находится в разработке. Список команд:\r\n\n/quiz *число вопросов* - запускает " +
                "мини-викторину со случайными вопросами на английском. Пишите ответ и в конце викторины узнаете," +
                "сколько очков Вы заработали. Вопросы бывают разной сложности и оцениваются разным числом очков." +
                "При желании можете писать боту любые сообщения - он просто перешлёт их обратно Вам.";

        ITransport tgBot = new TgTransport(TOKEN);

        System.out.println("PROGRAM STARTED!");

        tgBot
            .addCommandHandler("/start", ctx -> ctx.reply(startMessage))
            .addCommandHandler("/help", ctx -> ctx.reply(helpMessage))

            .addContextHandler("Copy message if not answered by commands",
                ctx -> !ctx.isAlreadyReplied()
                    ? ctx.reply(ctx.newMessage.getMessageText())
                    : ctx
            )

            .startBot();*/
        var a = Questions.nextQuestion();
        System.out.println("abc");
    }
}
