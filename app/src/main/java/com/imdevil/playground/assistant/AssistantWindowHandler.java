package com.imdevil.playground.assistant;

import com.imdevil.assistant.IAssistantHandler;
import com.imdevil.assistant.JsonRuleConfig;

public class AssistantWindowHandler implements IAssistantHandler {
    private static final String TAG = "AssistantWindowHandler";

    @JsonRuleConfig({
            @JsonRuleConfig.Rule(
                    name = "打开车窗",
                    fields = "object=车窗;feature=开度"
            ),
    })
    public void handleWindowIntent() {
        System.out.println("handleWindowIntent: 打开车窗");
    }
}
