package com.gosanon.javabotexample.main.quiz;

import com.gosanon.javabotexample.api.scenario.State;
import com.gosanon.javabotexample.api.scenario.StateScenario;
import com.gosanon.javabotexample.api.store.IUserStateStore;
import com.gosanon.javabotexample.api.store.implementations.RuntimeDB;
import com.gosanon.javabotexample.transports.implementations.TestStringTransport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.gosanon.javabotexample.main.CommonHandlers.*;
import static com.gosanon.javabotexample.main.Constants.*;
import static com.gosanon.javabotexample.main.quiz.QuizHandlers.*;
import static org.junit.jupiter.api.Assertions.*;

class QuizTest {
    String defaultStateName = "Default state";
    IUserStateStore runtimeDb = new RuntimeDB(defaultStateName);
    TestStringTransport testTransport = new TestStringTransport();
    StateScenario scenario = new StateScenario()
            .addState(new State("Default state")
                .addCommandHandler("/start", reply(startMessage))
                .addCommandHandler("/help", reply(helpMessage))
                .addCommandHandler("/quiz",
                   replyAndSetState("Введите число вопросов", "Quiz preparing")
                )
                .addContextHandler(notAnsweredThenCopy())
                )
            .addState(new State("Quiz preparing")
                .addContextHandler(ctx -> quizPreparing(ctx, QuestionProviderForTest.nextQuestion()))
            )

            .addState(new State("Quiz state")
                .addCommandHandler("/exit",
                   replyAndSetState("Отменяем викторину", "Default state")
                )
                .addCommandHandler("/help", reply(quizHelpMessage))
                .addContextHandler(ctx -> quizHandler(ctx, QuestionProviderForTest.nextQuestion()))
            )
            .addTransport(testTransport)
            .initWithStore(runtimeDb);

    @Test
    public void quizIsAvailable() {
        String[] userInput = { "/quiz" };
        String[] expectedOutput = { "Введите число вопросов" };
        testTransport.expectBehaviour(userInput, expectedOutput);
    }

    public void testOnIncorrectInput(String incorrectInput) {
        QuestionProviderForTest.refresh();
        ArrayList<String> userInput = new ArrayList<>();
        userInput.add("/quiz");
        userInput.add(incorrectInput);
        testTransport.processMessages(userInput.toArray(new String[0]));
        assertEquals(QuizDB.get(testTransport.senderId).questionNumber, 10);
        testTransport.clearChatHistory();
    }

    @Test
    public void makeQuizOf10QuestionsIfInputIsNotANumber() {
        testOnIncorrectInput("Я ввёл не число");
    }

    @Test
    public void makeQuizOf10QuestionsIfInputIsNotNatural() {
        testOnIncorrectInput("0");
    }
    public void commonUseTest(ArrayList<String>  userAnswers, int numberOfQuestions, int numberOfCorrectAnswers) {
        QuestionProviderForTest.refresh();
        ArrayList<String> userInput = new ArrayList<>();
        userInput.add("/quiz");
        userInput.add(String.valueOf(numberOfQuestions));
        userInput.addAll(userAnswers);
        testTransport.processMessages(userInput.toArray(new String[0]));
        assertEquals(QuizDB.get(testTransport.senderId).correctAnswerNumber, numberOfCorrectAnswers);
        testTransport.clearChatHistory();
    }

    @Test
    public void whenUserAnsweredWrong() {
        var userAnswers = new ArrayList<String>();
        var numberOfQuestions = 3;
        for (int i = 0; i < numberOfQuestions; i++){
            userAnswers.add("Я не знаю");
        }
        commonUseTest(userAnswers, numberOfQuestions, 0);
    }

    @Test
    public void whenUserAnsweredRight() {
        var userAnswers = new ArrayList<String>();
        var numberOfQuestions = 3;
        for (int i = 0; i < numberOfQuestions; i++){
            userAnswers.add(QuestionProviderForTest.questions.get(i).answer);
        }
        commonUseTest(userAnswers, numberOfQuestions, numberOfQuestions);
    }
}
