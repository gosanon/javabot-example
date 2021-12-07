package com.gosanon.javabotexample.main.questionsprovider;

import com.gosanon.javabotexample.api.scenario.State;
import com.gosanon.javabotexample.api.scenario.StateScenario;
import com.gosanon.javabotexample.api.store.IStore;
import com.gosanon.javabotexample.api.store.implementations.RuntimeDB;
import com.gosanon.javabotexample.api.transports.ITransport;
import com.gosanon.javabotexample.api.transports.implementations.TgTransport;
import com.gosanon.javabotexample.transports.implementations.TestStringTransport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.gosanon.javabotexample.main.CommonHandlers.*;
import static com.gosanon.javabotexample.main.Constants.*;
import static com.gosanon.javabotexample.main.questionsprovider.QuizHandlers.*;
import static org.junit.jupiter.api.Assertions.*;

class QuizHandlersTest {
    String defaultStateName = "Default state";
    IStore runtimeDb = new RuntimeDB(defaultStateName);
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

    @Test
    public void makeQuizOf10QuestionsIfInputIsIncorrect() {
        QuestionProviderForTest.prepareQuestions(1);
        ArrayList<String> userInput = new ArrayList<>();
        userInput.add("/quiz");
        userInput.add("Я ввёл не число");
        testTransport.processMessages(userInput.toArray(new String[0]));
        assertEquals(QuizDB.get(testTransport.senderId).questionNumber, 10);
        testTransport.clearChatHistory();
    }

    @Test
    public void whenUserAnsweredWrong() {
        var questionsCount = 3;
        QuestionProviderForTest.prepareQuestions(questionsCount + 1);
        ArrayList<String> userInput = new ArrayList<>();
        userInput.add("/quiz");
        userInput.add(String.valueOf(questionsCount));
        for (int i = 0; i < questionsCount; i++){
            userInput.add("Я не знаю");
        }
        testTransport.processMessages(userInput.toArray(new String[0]));
        assertEquals(QuizDB.get(testTransport.senderId).correctAnswerNumber, 0);
        testTransport.clearChatHistory();
    }

    @Test
    public void whenUserAnsweredRight() {
        var questionsCount = 3;
        QuestionProviderForTest.prepareQuestions(questionsCount + 1);
        ArrayList<String> userInput = new ArrayList<>();
        userInput.add("/quiz");
        userInput.add(String.valueOf(questionsCount));
        for (int i = 0; i < questionsCount; i++){
            userInput.add(QuestionProviderForTest.questions.get(i).answer);
        }
        testTransport.processMessages(userInput.toArray(new String[0]));
        assertEquals(QuizDB.get(testTransport.senderId).correctAnswerNumber, questionsCount);
        testTransport.clearChatHistory();
    }
}
