package com.gosanon.javabotexample.main.quiz;

import com.gosanon.javabotexample.api.scenario.Scenario;
import com.gosanon.javabotexample.api.scenario.Scene;
import com.gosanon.javabotexample.api.store.implementations.JsonStore;
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

        var jsonStore = new JsonStore<>("./QuizDB.json", DEFAULT_STATE,
            new StatsList(new CurrentQuizStats()));

        return new Scenario.Builder()
            .addScene(new Scene(DEFAULT_STATE)
                .addCommandHandler("/start", reply(START_MESSAGE))
                .addCommandHandler("/help", reply(HELP_MESSAGE))
                .addCommandHandler("/quiz", replyAndSetState(QUIZ_PREPARING_MESSAGE, QUIZ_PREPARING_STATE))
                .addCommandHandler("/leaderboard", getLeaderboardHandler())
                .addCommandHandler("/stats", getStatsHandler())
                .addContextHandler(notAnsweredThenCopy())
            )

            .addScene(new Scene(QUIZ_PREPARING_STATE)
                .addContextHandler(defaultQuizPreparing())
            )

            .addScene(new Scene(QUIZ_STATE)
                .addCommandHandler("/exit", replyAndSetState("Отменяем викторину", DEFAULT_STATE))
                .addCommandHandler("/help", reply(QUIZ_HELP_MESSAGE))
                .addContextHandler(defaultQuizHandler())
            )

            .build()

            .setUserStateManager(jsonStore);
    }
}
