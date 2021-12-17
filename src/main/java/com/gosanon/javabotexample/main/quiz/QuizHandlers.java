package com.gosanon.javabotexample.main.quiz;

import com.gosanon.javabotexample.api.scenario.context.EventContext;

import java.util.regex.Pattern;

public class QuizHandlers {
    public static QuizDB quizDB = new QuizDB(false);

    public static EventContext quizPreparing(EventContext ctx, Question questionSource){
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

        quizDB.putRecord(ctx.newMessage.getSenderId(), new UserQuizStats(numberOfQuestions));
        quizDB.currentStats(ctx.newMessage.getSenderId()).currentQuestion = questionSource;
        ctx.reply(errorMessage + "Начинаем викторину.\n\n" + quizDB
                        .currentStats(ctx.newMessage.getSenderId())
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

    public static EventContext quizHandler(EventContext ctx, Question questionSource){
        if(ctx.notYetReplied()){
            var userId = ctx.newMessage.getSenderId();

            var userStats = quizDB.currentStats(userId);
            userStats.answeredQuestionsNumber++;
            var userAnswer = ctx.newMessage.getMessageText();
            var messageAboutUserAnswer = checkAnswerAndUpdateUserStats(userAnswer, userId, questionSource);
            if (userStats.questionNumber == userStats.answeredQuestionsNumber){
                ctx.reply(messageAboutUserAnswer + "Викторина окончена. Итого:\n" + userStats)
                    .setState("Default state")
                    .sendPhoto("https://upload.wikimedia.org/wikipedia/en/e/e4/Green_tick.png");
                quizDB.updateUserData(userId);
            }
            else {
                ctx.reply(messageAboutUserAnswer + "Следующий вопрос:\n" + userStats.currentQuestion.question);
            }

        }
        return ctx;
    }

    private static String checkAnswerAndUpdateUserStats(String userAnswer, String userId, Question questionSource) {
        var userStats = quizDB.get(userId);
        var questionNumber = userStats.questionNumber;
        var answeredQuestionsNumber = userStats.answeredQuestionsNumber + 1;
        var correctAnswer = userStats.currentQuestion.answer;
        var correctAnswerNumber = userStats.correctAnswerNumber;
        var score = userStats.score;
        var question = userStats.currentQuestion;
        var result = "";
        if (userAnswer.equalsIgnoreCase(correctAnswer)){
            correctAnswerNumber++;
            score += question.value;
            result = "Правильно!\n";
        }
        else result = "Неправильно. Правильный ответ: " + correctAnswer + "\n";
        question = questionSource;
        quizDB.put(userId,
                new UserQuizStats(questionNumber, answeredQuestionsNumber, correctAnswerNumber, score, question));
        return result;
    }
}
