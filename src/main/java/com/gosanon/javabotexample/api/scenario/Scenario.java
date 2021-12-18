package com.gosanon.javabotexample.api.scenario;

import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.store.IUserStateManager;
import com.gosanon.javabotexample.api.transports.ITransport;

import java.util.HashMap;

public class Scenario {
    private final HashMap<String, Scene> scenes;
    private HashMap<String, ContextHandler> scenesHandlers;

    public ContextHandler complexScenarioHandler = ctx -> ctx;

    public Scenario(Builder scenarioBuilderObject) {
        this.scenes = scenarioBuilderObject.scenes;
    }

    public Scenario addTransport(ITransport transport) {
        transport.bindScenarioHandler(this);
        return this;
    }

    public Scenario initWithStore(IUserStateManager store) {
        for (String stateName: scenes.keySet()) {
            scenesHandlers.put(stateName, scenes.get(stateName).buildFinalHandler());
        }

        complexScenarioHandler = ctx -> {
            ctx.setStore(store);

            String userId = ctx.newMessage.getSenderId();
            String userState = store.getUserState(userId);
            ContextHandler userStateHandler = scenesHandlers.get(userState);

            return userStateHandler.apply(ctx);
        };

        return this;
    }

    public static class Builder {
        private final HashMap<String, Scene> scenes = new HashMap<>();

        public Builder addScene(Scene scene) {
            scenes.put(scene.getName(), scene);
            return this;
        }

        public Scenario build() {
            return new Scenario(this);
        }
    }
}
