package com.gosanon.javabotexample.transports;

import java.util.LinkedHashMap;
import java.util.Map;

public class __wip_ContextHandlerScene {
    private final String name;
    private final Map<String, __wip_ContextHandlerScene> childScenes = new LinkedHashMap<>();
    private __wip_ContextHandlerScene parentScene;

    __wip_ContextHandlerScene(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }

    public Map<String, __wip_ContextHandlerScene> getChildScenes() {
        return childScenes;
    }

    public __wip_ContextHandlerScene getParentScene() {
        return parentScene;
    }
}
