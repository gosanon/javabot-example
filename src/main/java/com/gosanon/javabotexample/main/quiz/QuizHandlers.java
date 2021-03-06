package com.gosanon.javabotexample.main.quiz;

import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.scenario.context.EventContext;
import com.gosanon.javabotexample.main.quiz.QuizDB;
import com.gosanon.javabotexample.main.quiz.questions.Question;
import com.gosanon.javabotexample.main.quiz.questions.QuestionsProvider;
import com.gosanon.javabotexample.main.quiz.stats.CurrentQuizStats;

import static com.gosanon.javabotexample.main.Constants.*;

public class QuizHandlers {
    public static QuizDB quizDB = new QuizDB();

    public static ContextHandler defaultQuizHandler() {
        return ctx -> quizHandler(ctx, QuestionsProvider.nextQuestion());
    }

    public static ContextHandler defaultQuizPreparing() {
        return ctx -> quizPreparing(ctx, QuestionsProvider.nextQuestion());
    }

    public static ContextHandler getStatsHandler() {
        return ctx -> ctx.reply(quizDB.getOverallStats(ctx.newMessage.getSenderId()).toString());
    }

    public static ContextHandler getLeaderboardHandler() {
        return ctx -> ctx.reply(quizDB.leaderboard.toString());
    }

    public static EventContext quizPreparing(EventContext ctx, Question questionSource){
        var numberOfQuestions = 0;
        var startMessage = "Начинаем викторину.\n\n";
        try {
            numberOfQuestions = Integer.parseInt(ctx.newMessage.getMessageText());
            if(numberOfQuestions < 1)
                throw new IllegalArgumentException("Введено ненатуральное число");
        }
        catch (Exception e){
            startMessage = INCORRECT_INPUT_WARNING + startMessage;
            numberOfQuestions = 10;
        }
        quizDB.putRecord(ctx.newMessage.getSenderId(),
                new CurrentQuizStats(numberOfQuestions, 0, 0, 0, questionSource));
        ctx.reply(startMessage + quizDB
                        .getCurrentQuizStats(ctx.newMessage.getSenderId())
                        .currentQuestion
                        .question)
                .toScene(QUIZ_STATE);
        return ctx;
    }

    public static EventContext quizHandler(EventContext ctx, Question questionSource){
        if(ctx.notYetReplied()){
            var userId = ctx.newMessage.getSenderId();
            var userAnswer = ctx.newMessage.getMessageText();
            var messageAboutUserAnswer = checkAnswer(userAnswer, userId, questionSource);
            var userStats = quizDB.getCurrentQuizStats(userId);
            if (userStats.questionsInQuiz == userStats.answeredQuestionsNumber){
                ctx.reply(String.format(QUIZ_PASS_MESSAGE, messageAboutUserAnswer, userStats))
                        .toScene(DEFAULT_STATE)
                        .sendPhoto(QUIZ_PASS_IMAGE_URL);
                quizDB.updateUserStats(userId);
            }
            else {
                ctx.reply(String.format(NEXT_QUESTION_MESSAGE,
                        messageAboutUserAnswer, userStats.currentQuestion.question));
            }
        }
        return ctx;
    }

    private static String checkAnswer(String userAnswer, String userId, Question questionSource) {
        var result = "";
        var userStats = quizDB.getCurrentQuizStats(userId);
        var questionNumber = userStats.questionsInQuiz;
        var answeredQuestionsNumber = userStats.answeredQuestionsNumber + 1;
        var correctAnswerNumber = userStats.correctAnswerNumber;
        var score = userStats.score;
        var correctAnswer = userStats.currentQuestion.answer;
        if (userAnswer.equalsIgnoreCase(correctAnswer)){
            correctAnswerNumber++;
            score += userStats.currentQuestion.value;
            result = CORRECT_ANSWER_MESSAGE;
        }
        else result = String.format(WRONG_ANSWER_MESSAGE, correctAnswer);
        var updatedQuizStats = new CurrentQuizStats(
                questionNumber, answeredQuestionsNumber, correctAnswerNumber, score, questionSource);
        quizDB.putRecord(userId, updatedQuizStats);
        return result;
    }
}
