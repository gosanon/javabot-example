package com.gosanon.javabotexample.api.transports;

import com.gosanon.javabotexample.api.scenario.Scenario;

import java.util.ArrayList;

public abstract class CommonTransport implements ITransport {
    protected final ArrayList<Scenario> boundScenarios = new ArrayList<>();

    public void bindScenarioHandler(Scenario scenario) {
        boundScenarios.add(scenario);
    }

    abstract public void sendMessage(String targetId, String messageText);
}
