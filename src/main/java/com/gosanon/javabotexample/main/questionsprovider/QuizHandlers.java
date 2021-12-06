package com.gosanon.javabotexample.main.questionsprovider;

import com.gosanon.javabotexample.api.scenario.context.EventContext;
import com.gosanon.javabotexample.main.questionsprovider.QuestionsProvider;
import com.gosanon.javabotexample.main.questionsprovider.UserQuizStats;

import java.util.HashMap;
import java.util.regex.Pattern;

public class QuizHandlers {
    static HashMap<String, UserQuizStats> QuizDB = new HashMap<>();

    public static EventContext quizPreparing(EventContext ctx){
        var numberRegex = "(^\\d+$)";
        var numberOfQuestions = 0;
        var errorMessage = "";
        try {
            numberOfQuestions = Integer.parseInt(
                    findMatchByRegex(numberRegex, ctx.newMessage.getMessageText()));
            if(numberOfQuestions < 1)
                throw new Exception("Ненатуральное число");
        }
        catch (Exception e){
            errorMessage = "Бот не понял Ваш ввод, так что будет 10 вопросов.\n";
            numberOfQuestions = 10;
        }
        QuizDB.put(ctx.newMessage.getSenderId(), new UserQuizStats(numberOfQuestions));
        QuizDB.get(ctx.newMessage.getSenderId()).currentQuestion = QuestionsProvider.nextQuestion();
        ctx.reply(errorMessage + "Начинаем викторину.\n\n" + QuizDB
                        .get(ctx.newMessage.getSenderId())
                        .currentQuestion
                        .question)
                .setState("Quiz state");
        return ctx;
    }

    static String findMatchByRegex(String regex, String text){
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static EventContext quizHandler(EventContext ctx){
        var userStats = QuizDB.get(ctx.newMessage.getSenderId());
        userStats.answeredQuestionsNumber++;
        var userAnswer = ctx.newMessage.getMessageText();
        var messageAboutUserAnswer = checkAnswer(userAnswer, userStats);
        if (userStats.questionNumber == userStats.answeredQuestionsNumber){
            ctx.reply(String.format(
                            """
                                    Викторина окончена. Итого:
                                    Всего вопросов: %d
                                    Правильных ответов: %d
                                    Очков набрано: %d""",
                            userStats.questionNumber, userStats.correctAnswerNumber, userStats.score))
                    .setState("Default state");
            QuizDB.remove(ctx.newMessage.getSenderId());
        }
        else {
            userStats.currentQuestion = QuestionsProvider.nextQuestion();
            ctx.reply(messageAboutUserAnswer + "Следующий вопрос:\n" + userStats.currentQuestion.question);
        }
        return ctx;
    }

    private static String checkAnswer(String userAnswer, UserQuizStats userStats) {

        var correctAnswer = userStats.currentQuestion.answer;
        if (userAnswer.equalsIgnoreCase(correctAnswer)){
            userStats.correctAnswerNumber++;
            userStats.score += userStats.currentQuestion.value;
            return "Правильно!\n";
        }
        else return "Неправильно. Правильный ответ: " + correctAnswer + "\n";
    }
}
