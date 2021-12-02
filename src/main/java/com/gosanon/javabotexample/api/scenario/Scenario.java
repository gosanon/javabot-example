package com.gosanon.javabotexample.api.scenario;

import com.gosanon.javabotexample.api.transports.ITransport;

import java.util.HashMap;

public class Scenario {
    private final HashMap<String, State> states = new HashMap<>();

    Scenario addState(State state) {
        states.put(state.getName(), state);
        return this;
    }

    Scenario addInputTransport(ITransport transport) {
        // TODO
        return this;
    }

    Scenario addOutputTransport() {
        // TODO
        return this;
    }

    Scenario addIOTransport() {
        // TODO
        return this;
    }
}
