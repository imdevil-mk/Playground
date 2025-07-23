package com.imdevil.playground.assistant;

import com.imdevil.assistant.Assistant;

public class AssistantManager {
    private static final String TAG = "AssistantManager";

    public static class AssistantManagerHolder {
        private static final AssistantManager INSTANCE = new AssistantManager();
    }

    private AssistantManager() {

    }

    public static AssistantManager getInstance() {
        return AssistantManagerHolder.INSTANCE;
    }

    public void init() {
        Assistant.getInstance().init();
        Assistant.getInstance()
                .addHandler(new AssistantSeatHandler())
                .addHandler(new AssistantWindowHandler())
        ;
    }

    public void dispatch(String json) {
        Assistant.getInstance().dispatchJson(json);
    }
}
