package com.gosanon.javabotexample.main;

import com.gosanon.javabotexample.api.scenario.State;
import com.gosanon.javabotexample.api.scenario.StateScenario;
import com.gosanon.javabotexample.api.store.IUserStateStore;
import com.gosanon.javabotexample.api.store.implementations.RuntimeDB;
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
        IUserStateStore runtimeDb = new RuntimeDB(defaultStateName);

        // Init transports
        ITransport tgBot = new TgTransport(TOKEN);

        // Create scenario
        new StateScenario()

            // Add states
            .addState(new State("Default state")

                // Add handlers
                .addCommandHandler("/start", reply(START_MESSAGE))
                .addCommandHandler("/help", reply(HELP_MESSAGE))
                .addCommandHandler("/quiz",
                    replyAndSetState("Введите число вопросов", "Quiz preparing")
                )
                .addCommandHandler("/leaderboard", reply(quizDB.printLeaderboard()))
                .addCommandHandler("/stats",
                        ctx -> ctx.reply(quizDB.getOverallStats(ctx.newMessage.getSenderId()).toString()))
                .addContextHandler(notAnsweredThenCopy())
            )

            .addState(new State("Quiz preparing")
                .addContextHandler(ctx -> quizPreparing(ctx, QuestionsProvider.nextQuestion()))
            )

            .addState(new State("Quiz state")
                .addCommandHandler("/exit",
                    replyAndSetState("Отменяем викторину", "Default state")
                )
                .addCommandHandler("/help", reply(QUIZ_HELP_MESSAGE))
                .addContextHandler(ctx -> quizHandler(ctx, QuestionsProvider.nextQuestion()))
            )

            // Add transports
            .addTransport(tgBot)

            // Init scenario
            .initWithStore(runtimeDb);
    }


}
