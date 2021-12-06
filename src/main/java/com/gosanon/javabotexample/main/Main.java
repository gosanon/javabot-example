package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.State;
import com.gosanon.javabotexample.api.scenario.StateScenario;
import com.gosanon.javabotexample.api.store.IStore;
import com.gosanon.javabotexample.api.store.implementations.RuntimeDB;
import com.gosanon.javabotexample.api.transports.ITransport;
import com.gosanon.javabotexample.api.transports.implementations.TgTransport;
import com.gosanon.javabotexample.main.questionsprovider.QuizHandlers;

import static com.gosanon.javabotexample.main.CommonHandlers.*;
import static com.gosanon.javabotexample.main.Constants.*;

public class Main {

    public static void main(String[] args) {
        // Constants
        String defaultStateName = "Default state";

        // Prepare data and db
        String TOKEN = System.getenv("JAVABOT_TOKEN_TG");
        IStore runtimeDb = new RuntimeDB(defaultStateName);

        // Init transports
        ITransport tgBot = new TgTransport(TOKEN);

        // Create scenario
        new StateScenario()

            // Add states
            .addState(new State("Default state")

                // Add handlers
                .addCommandHandler("/start", reply(startMessage))
                .addCommandHandler("/help", reply(helpMessage))
                .addCommandHandler("/quiz",
                    replyAndSetState("Введите число вопросов", "Quiz preparing")
                )
                .addContextHandler(notAnsweredThenCopy())
            )

            .addState(new State("Quiz preparing")
                .addContextHandler(QuizHandlers::quizPreparing)
            )

            .addState(new State("Quiz state")
                .addCommandHandler("/exit",
                    replyAndSetState("Отменяем викторину", "Default state")
                )
                .addCommandHandler("/help", reply(quizHelpMessage))
                .addContextHandler(QuizHandlers::quizHandler)
            )

            // Add transports
            .addTransport(tgBot)

            // Init scenario
            .initWithStore(runtimeDb);
    }


}
