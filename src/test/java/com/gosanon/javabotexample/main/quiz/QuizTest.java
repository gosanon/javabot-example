package com.gosanon.javabotexample.main.quiz;

import com.gosanon.javabotexample.api.scenario.Scene;
import com.gosanon.javabotexample.api.scenario.Scenario;
import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.store.IUserStateManager;
import com.gosanon.javabotexample.api.store.implementations.RuntimeStateManager;
import com.gosanon.javabotexample.transports.implementations.TestStringTransport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gosanon.javabotexample.main.CommonHandlers.*;
import static com.gosanon.javabotexample.main.Constants.*;
import static com.gosanon.javabotexample.main.quiz.QuizHandlers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuizTest {
    IUserStateManager runtimeDb = new RuntimeStateManager(DEFAULT_STATE);
    TestStringTransport testTransport = new TestStringTransport();
    Scenario scenario = new Scenario.Builder()
            .addScene(new Scene(DEFAULT_STATE)
                .addCommandHandler("/start", reply(START_MESSAGE))
                .addCommandHandler("/help", reply(HELP_MESSAGE))
                .addCommandHandler("/quiz",
                   replyAndSetState("Введите число вопросов", QUIZ_PREPARING_STATE)
                )
                .addCommandHandler("/leaderboard", ctx -> ctx.reply(quizDB.leaderboard.toString()))
                .addContextHandler(notAnsweredThenCopy())
                )
            .addScene(new Scene(QUIZ_PREPARING_STATE)
                .addContextHandler(testQuizHPreparing())
            )

            .addScene(new Scene(QUIZ_STATE)
                .addCommandHandler("/exit",
                   replyAndSetState("Отменяем викторину", DEFAULT_STATE)
                )
                .addCommandHandler("/help", reply(QUIZ_HELP_MESSAGE))
                .addContextHandler(testQuizHandler())
            )
            .build()
            .addTransport(testTransport)
            .setUserStateManager(runtimeDb)
            .init();


    public static ContextHandler testQuizHandler() {
        return ctx -> quizHandler(ctx, QuestionProviderForTest.nextQuestion());
    }

    public static ContextHandler testQuizHPreparing() {
        return ctx -> quizPreparing(ctx, QuestionProviderForTest.nextQuestion());
    }

    @Test
    public void quizIsAvailable() {
        testTransport.senderId = "Alice";
        String[] userInput = { "/quiz" };
        String[] expectedOutput = { "Введите число вопросов" };
        testTransport.expectBehaviour(userInput, expectedOutput);
    }

    @Test
    public void makeQuizOf10QuestionsIfInputIsNotANumber() {
        testTransport.senderId = "Charlie";
        runQuiz(Arrays.asList("Я ввёл не число"), false);
        assertEquals(10, quizDB.getCurrentQuizStats(testTransport.senderId).questionsInQuiz);
    }

    @Test
    public void makeQuizOf10QuestionsIfInputIsNotNatural() {
        testTransport.senderId = "Donald";
        runQuiz(Arrays.asList("-15"), false);
        assertEquals(10, quizDB.getCurrentQuizStats(testTransport.senderId).questionsInQuiz);
    }

    public void runQuiz(List<String> userAnswers, boolean addSize) {
        QuestionProviderForTest.refresh();
        ArrayList<String> userInput = new ArrayList<>();
        userInput.add("/quiz");
        if(addSize)
            userInput.add(String.valueOf(userAnswers.size()));
        userInput.addAll(userAnswers);
        testTransport.processMessages(userInput.toArray(new String[0]));
        testTransport.clearChatHistory();
    }

    @Test
    public void whenUserAnsweredWrong() {
        testTransport.senderId = "Eugen";
        var userAnswers = new ArrayList<String>();
        var numberOfQuestions = 3;
        for (int i = 0; i < numberOfQuestions; i++){
            userAnswers.add("Я не знаю");
        }
        runQuiz(userAnswers, true);
        assertEquals(0, quizDB.getOverallStats(testTransport.senderId).correctAnswerNumber);
    }

    @Test
    public void whenUserAnsweredRight() {
        testTransport.senderId = "Felix";
        var userAnswers = new ArrayList<String>();
        var numberOfQuestions = 3;
        for (int i = 0; i < numberOfQuestions; i++){
            userAnswers.add(QuestionProviderForTest.questions.get(i).answer);
        }
        runQuiz(userAnswers, true);
        assertEquals(numberOfQuestions, quizDB.getOverallStats(testTransport.senderId).correctAnswerNumber);
    }

    @Test
    public void leaderboardIsNotEmptyAfterQuiz(){
        testTransport.senderId = "George";
        var numberOfQuestions = 3;
        ArrayList<String> userInput = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++){
            userInput.add(QuestionProviderForTest.questions.get(i).answer);
        }
        runQuiz(userInput, true);
        assertTrue(quizDB.leaderboard.size() > 0);
    }

    @Test
    public void leaderboardWorksForMultipleUsers(){
        testTransport.senderId = "Harrison";
        var sizeBeforeTest = quizDB.leaderboard.size();
        var numberOfQuestions = 3;

        ArrayList<String> firstUserInput = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++){
            firstUserInput.add(QuestionProviderForTest.questions.get(i).answer);
        }
        runQuiz(firstUserInput, true);

        testTransport.senderId = "Ian";
        ArrayList<String> secondUserInput = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++){
            secondUserInput.add(QuestionProviderForTest.questions.get(i).answer);
        }
        runQuiz(secondUserInput, true);

        var sizeAfterTest = quizDB.leaderboard.size() - sizeBeforeTest;
        assertEquals(2, sizeAfterTest);
    }

    @Test
    public void statsSavedFromMultipleGames(){
        var id = "Bob";
        testTransport.senderId = id;
        var repeats = 3;
        var numberOfQuestions = 3;
        var score = 0;
        ArrayList<String> userInput = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++){
            userInput.add(QuestionProviderForTest.questions.get(i).answer);
            score += QuestionProviderForTest.questions.get(i).value;
        }
        for (int i = 0; i < repeats; i++)
            runQuiz(userInput, true);
        assertEquals(repeats * numberOfQuestions, quizDB.getOverallStats(id).answeredQuestionsNumber);
        assertEquals(repeats * numberOfQuestions, quizDB.getOverallStats(id).correctAnswerNumber);
        assertEquals(repeats * score, quizDB.getOverallStats(id).score);
    }

}
