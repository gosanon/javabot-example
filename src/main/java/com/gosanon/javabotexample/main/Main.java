package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.State;
import com.gosanon.javabotexample.api.scenario.StateScenario;
import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.store.IStore;
import com.gosanon.javabotexample.api.store.implementations.RuntimeDB;
import com.gosanon.javabotexample.api.transports.ITransport;
import com.gosanon.javabotexample.api.transports.implementations.TgTransport;

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
                .addCommandHandler("SECOND STATE",
                    replyAndSetState("Переход во второе состояние", "Second state")
                )
                .addContextHandler(notAnsweredThenCopy())
            )

            .addState(new State("Second state")
                .addCommandHandler("GO BACK",
                    replyAndSetState("Переход в состояние по умолчанию", "Default state")
                )
                .addContextHandler(notAnsweredThenCopy())
            )

            // Add transports
            .addTransport(tgBot)

            // Init scenario
            .initWithStore(runtimeDb);
    }
}
