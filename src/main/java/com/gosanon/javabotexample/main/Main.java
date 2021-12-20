package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.Scene;
import com.gosanon.javabotexample.api.scenario.Scenario;
import com.gosanon.javabotexample.api.store.IUserStateManager;
import com.gosanon.javabotexample.api.store.implementations.RuntimeStateManager;
import com.gosanon.javabotexample.api.transports.ITransport;
import com.gosanon.javabotexample.api.transports.implementations.TgTransport;
import com.gosanon.javabotexample.main.quiz.questions.QuestionsProvider;

import static com.gosanon.javabotexample.main.CommonHandlers.*;
import static com.gosanon.javabotexample.main.Constants.*;
import static com.gosanon.javabotexample.main.quiz.QuizHandlers.*;
import static com.gosanon.javabotexample.main.quiz.QuizScenarioFactory.createQuizScenario;

public class Main {

    public static void main(String[] args) {
        String TOKEN = System.getenv("JAVABOT_TOKEN_TG");
        ITransport tgBot = new TgTransport(TOKEN);

        Scenario QuizScenario = createQuizScenario(true)
            .addTransport(tgBot)
            .init();
    }


}
