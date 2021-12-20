package com.gosanon.javabotexample.main.quiz;

import com.gosanon.javabotexample.api.scenario.Scenario;
import com.gosanon.javabotexample.api.scenario.Scene;
import com.gosanon.javabotexample.api.store.implementations.JsonStore;
import com.gosanon.javabotexample.main.quiz.questions.QuestionsProvider;
import com.gosanon.javabotexample.main.quiz.stats.CurrentQuizStats;
import com.gosanon.javabotexample.main.quiz.stats.StatsList;

import static com.gosanon.javabotexample.main.CommonHandlers.*;
import static com.gosanon.javabotexample.main.Constants.*;
import static com.gosanon.javabotexample.main.quiz.QuizHandlers.*;

public class QuizScenarioFactory {
    public static Scenario createQuizScenario(boolean storageRequiresManualSynchronisation) {
        if (!storageRequiresManualSynchronisation) {
            throw new RuntimeException("Not implemented with manual synchronisation parametrisation. Use 'true'");
        }

        // Constants
        String defaultStateName = "Default state";

        // Store
        var jsonStore = new JsonStore<>("./QuizDB.json", defaultStateName,
            new StatsList(new CurrentQuizStats()));

        return new Scenario.Builder()

            // Add states
            .addScene(new Scene("Default state")

                // Add handlers
                .addCommandHandler("/start", reply(START_MESSAGE))
                .addCommandHandler("/help", reply(HELP_MESSAGE))
                .addCommandHandler("/quiz",
                    replyAndSetState("Введите число вопросов", "QuizScenarioFactory preparing")
                )
                .addCommandHandler("/leaderboard", reply(quizDB.leaderboard.toString()))
                .addCommandHandler("/stats",
                    ctx -> ctx.reply(quizDB.getOverallStats(ctx.newMessage.getSenderId()).toString()))
                .addContextHandler(notAnsweredThenCopy())
            )

            .addScene(new Scene("QuizScenarioFactory preparing")
                .addContextHandler(ctx -> quizPreparing(ctx, QuestionsProvider.nextQuestion()))
            )

            .addScene(new Scene("QuizScenarioFactory state")
                .addCommandHandler("/exit",
                    replyAndSetState("Отменяем викторину", "Default state")
                )
                .addCommandHandler("/help", reply(QUIZ_HELP_MESSAGE))
                .addContextHandler(ctx -> quizHandler(ctx, QuestionsProvider.nextQuestion()))
            )

            // Stop building
            .build()

            .setUserStateManager(jsonStore);
    }
}
