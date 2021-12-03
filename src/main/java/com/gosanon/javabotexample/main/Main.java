package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.State;
import com.gosanon.javabotexample.api.scenario.StateScenario;
import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.store.IStore;
import com.gosanon.javabotexample.api.store.implementations.RuntimeDB;
import com.gosanon.javabotexample.api.transports.ITransport;
import com.gosanon.javabotexample.api.transports.implementations.TgTransport;

import static com.gosanon.javabotexample.main.Constants.*;

public class Main {
    public static void main(String[] args) {
        // Constants
        String defaultStateName = "Default state";

        // Common handlers
        ContextHandler copyUserMessageHandler =
            ctx -> ctx.notYetReplied()
                ? ctx.reply(ctx.newMessage.getMessageText())
                : ctx;

        // Prepare data
        String TOKEN = System.getenv("JAVABOT_TOKEN_TG");
        IStore runtimeDb = new RuntimeDB(defaultStateName);

        // Init transports
        ITransport tgBot = new TgTransport(TOKEN);

        // Create scenario
        new StateScenario()

            // Add states
            .addState(new State("Default state")

                // Add handlers
                .addCommandHandler("/start", ctx -> ctx.reply(startMessage))
                .addCommandHandler("/help", ctx -> ctx.reply(helpMessage))
                .addCommandHandler("SECOND STATE",
                    ctx -> ctx
                        .reply("Переход во второе состояние")
                        .setState("Second state")
                )
                .addContextHandler("Copy message if not answered by commands", copyUserMessageHandler)
            )

            .addState(new State("Second state")
                .addCommandHandler("GO BACK",
                    ctx -> ctx
                        .reply("Переход в состояние по умолчанию")
                        .setState("Default state")
                )
                .addContextHandler("Copy message if not answered by commands", copyUserMessageHandler)
            )

            // Add transports
            .addTransport(tgBot)

            // Init scenario
            .initWithStore(runtimeDb);
    }
}
