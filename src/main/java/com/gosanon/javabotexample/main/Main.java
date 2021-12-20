package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.Scenario;
import com.gosanon.javabotexample.api.transports.ITransport;
import com.gosanon.javabotexample.api.transports.implementations.TgTransport;

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
