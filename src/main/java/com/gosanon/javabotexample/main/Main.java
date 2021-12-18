package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.Scene;
import com.gosanon.javabotexample.api.scenario.Scenario;
import com.gosanon.javabotexample.api.store.IUserStateManager;
import com.gosanon.javabotexample.api.store.implementations.RuntimeStateManager;
import com.gosanon.javabotexample.api.transports.ITransport;
import com.gosanon.javabotexample.api.transports.implementations.TgTransport;
import com.gosanon.javabotexample.main.quiz.QuestionsProvider;

import static com.gosanon.javabotexample.main.CommonHandlers.*;
import static com.gosanon.javabotexample.main.Constants.*;
import static com.gosanon.javabotexample.main.quiz.QuizHandlers.*;

public class Main {

    public static void main(String[] args) {
        // Constants
        String defaultStateName = "Default state";

        // Prepare data and db
        String TOKEN = System.getenv("JAVABOT_TOKEN_TG");
        IUserStateManager runtimeDb = new RuntimeStateManager(defaultStateName);

        // Init transports
        ITransport tgBot = new TgTransport(TOKEN);

        // Create scenario builder
        new Scenario.Builder()

            // Add states
            .addScene(new Scene("Default state")

                // Add handlers
                .addCommandHandler("/start", reply(startMessage))
                .addCommandHandler("/help", reply(helpMessage))
                .addCommandHandler("/quiz",
                    replyAndSetState("Введите число вопросов", "Quiz preparing")
                )
                .addCommandHandler("/leaderboard", reply(quizDB.getLeaderboardString()))
                .addCommandHandler("/stats",
                        ctx -> ctx.reply(quizDB.overallStats(ctx.newMessage.getSenderId()).toString()))
                .addContextHandler(notAnsweredThenCopy())
            )

            .addScene(new Scene("Quiz preparing")
                .addContextHandler(ctx -> quizPreparing(ctx, QuestionsProvider.nextQuestion()))
            )

            .addScene(new Scene("Quiz state")
                .addCommandHandler("/exit",
                    replyAndSetState("Отменяем викторину", "Default state")
                )
                .addCommandHandler("/help", reply(quizHelpMessage))
                .addContextHandler(ctx -> quizHandler(ctx, QuestionsProvider.nextQuestion()))
            )

            // Stop building
            .build()

            // Add transports
            .addTransport(tgBot)

            // Init scenario
            .initWithStore(runtimeDb);
    }


}
