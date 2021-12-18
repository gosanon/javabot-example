package com.gosanon.javabotexample.main.quiz;

import com.gosanon.javabotexample.api.scenario.Scene;
import com.gosanon.javabotexample.api.scenario.Scenario;
import com.gosanon.javabotexample.api.store.IUserStateManager;
import com.gosanon.javabotexample.api.store.implementations.RuntimeDB;
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
    String defaultStateName = "Default state";
    IUserStateManager runtimeDb = new RuntimeDB(defaultStateName);
    TestStringTransport testTransport = new TestStringTransport();
    Scenario scenario = new Scenario.Builder()
        .addScene(new Scene("Default state")
            .addCommandHandler("/start", reply(startMessage))
            .addCommandHandler("/help", reply(helpMessage))
            .addCommandHandler("/quiz",
               replyAndSetState("Введите число вопросов", "Quiz preparing")
            )
            .addCommandHandler("/leaderboard", reply(quizDB.printLeaderboard()))
            .addContextHandler(notAnsweredThenCopy())
            )
        .addScene(new Scene("Quiz preparing")
            .addContextHandler(ctx -> quizPreparing(ctx, QuestionProviderForTest.nextQuestion()))
        )

        .addScene(new Scene("Quiz state")
            .addCommandHandler("/exit",
               replyAndSetState("Отменяем викторину", "Default state")
            )
            .addCommandHandler("/help", reply(quizHelpMessage))
            .addContextHandler(ctx -> quizHandler(ctx, QuestionProviderForTest.nextQuestion()))
        )
        .build()
        .addTransport(testTransport)
        .initWithStore(runtimeDb);

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
        assertEquals(10, quizDB.currentStats(testTransport.senderId).questionNumber);
    }

    @Test
    public void makeQuizOf10QuestionsIfInputIsNotNatural() {
        testTransport.senderId = "Donald";
        runQuiz(Arrays.asList("Я ввёл не число"), false);
        assertEquals(10, quizDB.currentStats(testTransport.senderId).questionNumber);
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
        assertEquals(0, quizDB.overallStats(testTransport.senderId).correctAnswerNumber);
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
        assertEquals(numberOfQuestions, quizDB.overallStats(testTransport.senderId).correctAnswerNumber);
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
        assertEquals(repeats * numberOfQuestions, quizDB.overallStats(id).questionNumber);
        assertEquals(repeats * numberOfQuestions, quizDB.overallStats(id).correctAnswerNumber);
        assertEquals(repeats * score, quizDB.overallStats(id).score);
    }

}
