package com.gosanon.javabotexample.api.scenario;

import com.gosanon.javabotexample.api.scenario.context.ContextHandler;
import com.gosanon.javabotexample.api.store.IStore;
import com.gosanon.javabotexample.api.transports.ITransport;

import java.util.ArrayList;
import java.util.HashMap;

public class StateScenario {
    private final HashMap<String, State> states = new HashMap<>();
    private final HashMap<String, ContextHandler> statesHandlers = new HashMap<>();

    /* inputTransports list can be used in "polling" model */
    //private final ArrayList<ITransport> inputTransports = new ArrayList<>();
    private final ArrayList<ITransport> outputTransports = new ArrayList<>();

    public ContextHandler complexScenarioHandler = ctx -> ctx;

    public StateScenario addState(State state) {
        states.put(state.getName(), state);
        return this;
    }

    private StateScenario addInputTransport(ITransport transport) {
        transport.bindScenarioHandler(this);
        return this;
    }

    private StateScenario addOutputTransport(ITransport transport) {
        outputTransports.add(transport);
        return this;
    }

    public StateScenario addTransport(ITransport transport) {
        addInputTransport(transport);
        addOutputTransport(transport);
        return this;
    }

    public StateScenario initWithStore(IStore store) {
        for (String stateName: states.keySet()) {
            statesHandlers.put(stateName, states.get(stateName).buildFinalHandler());
        }

        complexScenarioHandler = ctx -> {
            ctx.setStore(store);

            String userId = ctx.newMessage.getSenderId();
            String userState = store.getRecord(userId);
            ContextHandler userStateHandler = statesHandlers.get(userState);

            return userStateHandler.apply(ctx);
        };

        return this;
    }
}
