package com.gosanon.javabotexample.api.transports;

import com.gosanon.javabotexample.api.scenario.ScenarioHandler;
import com.gosanon.javabotexample.api.scenario.StateScenario;

import java.util.ArrayList;

public abstract class CommonTransport implements ITransport {
    protected final ArrayList<StateScenario> boundScenarios = new ArrayList<>();

    public void bindScenarioHandler(StateScenario scenario) {
        boundScenarios.add(scenario);
    }

    abstract public void sendMessage(String targetId, String messageText);
}
