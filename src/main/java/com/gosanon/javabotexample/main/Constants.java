package com.gosanon.javabotexample.main;

public class Constants {
    public static final String startMessage = String.join("\n"
        ,"Пишите /quiz для игры в викторину."
        , ""
        , "Подробнее: /help");

    public static final String helpMessage = String.join("\n"
        , "Бот находится в разработке. Список команд:"
        , ""
        , "/quiz - запускает мини-викторину со случайными вопросами на английском языке."
        , ""
        , "Вопросы бывают разной сложности и оцениваются разным числом очков."
        , "Пишите ответ и в конце викторины узнаете, сколько очков Вы заработали."
        , ""
        , "При желании можете писать боту любые сообщения - он просто перешлёт их обратно Вам.");

    public static final String quizHelpMessage = String.join("\n"
        , "Сейчас Вы проходите викторину."
        , ""
        , "Если хотите прекратить её прохождение, напишите команду /exit");
}
