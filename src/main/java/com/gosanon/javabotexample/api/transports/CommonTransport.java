package com.gosanon.javabotexample.api.transports;

import com.gosanon.javabotexample.api.scenario.ScenarioHandler;
import com.gosanon.javabotexample.api.scenario.StateScenario;

import java.util.ArrayList;

public abstract class CommonTransport implements ITransport {
    protected final ArrayList<StateScenario> boundScenarios = new ArrayList<>();
    private final ArrayList<ScenarioHandler> onScenarioBindHandlers = new ArrayList<>();

    private void onScenarioBind(StateScenario scenario) {
        for (var handler: onScenarioBindHandlers) {
            // This also allows scenario mutations from CommonTransport inheritors.
            handler.apply(scenario);
        }
    }

    /*
        Example use: in test transport, add handler to generate output to memory.
        It was possible to create an empty method so that it would be overridden later;
        However I decided to do it this way to make this part of abstract class functionality more fixed.
    */
    protected ITransport addOnScenarioBindHandler(ScenarioHandler handler) {
        onScenarioBindHandlers.add(handler);
        return this;
    }

    public void bindScenarioHandler(StateScenario scenario) {
        boundScenarios.add(scenario);
        onScenarioBind(scenario);
    }

    abstract public void sendMessage(String targetId, String messageText);
}
