package com.gosanon.javabotexample.main;

public class Constants {
    public static final String START_MESSAGE = """
        Пишите /quiz для игры в викторину."
        
        Подробнее: /help""";

    public static final String HELP_MESSAGE = """
        Бот находится в разработке. Список команд:
        
        /quiz - запускает мини-викторину со случайными вопросами на английском языке.
        Вопросы бывают разной сложности и оцениваются разным числом очков.
        Пишите ответ и в конце викторины узнаете, сколько очков Вы заработали.
        
        /leaderboard - выводит список лидеров по набранным в викторине очкам
        
        /stats - выводит Вашу статистику викторины за всё время
        
        При желании можете писать боту любые сообщения - он просто перешлёт их обратно Вам.""";

    public static final String QUIZ_HELP_MESSAGE = """
        Сейчас Вы проходите викторину.

        Если хотите прекратить её прохождение, напишите команду /exit""";

    public static final String INCORRECT_INPUT_WARNING = "Бот не понял Ваш ввод, так что будет 10 вопросов.\n";

    public static final String QUIZ_PASS_IMAGE_URL = "https://upload.wikimedia.org/wikipedia/en/e/e4/Green_tick.png";

    public static final String QUIZ_PASS_MESSAGE = """
                                %sВикторина окончена. Итого:%n%s
                                """;

    public static final String NEXT_QUESTION_MESSAGE = """
                                %sСледующий вопрос:%n%s
                                """;
    public static final String WRONG_ANSWER_MESSAGE = """
                                Неправильно. Правильный ответ: %s%n
                                """;
    public static final String CORRECT_ANSWER_MESSAGE = "Правильно!\n";
}
