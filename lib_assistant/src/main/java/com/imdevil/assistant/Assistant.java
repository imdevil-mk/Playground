package com.imdevil.assistant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Assistant {
    private static final String TAG = "Assistant";

    private static class AssistantInstanceHolder {
        public static final Assistant INSTANCE = new Assistant();
    }

    private Assistant() {

    }

    public static Assistant getInstance() {
        return AssistantInstanceHolder.INSTANCE;
    }

    private JsonDispatcher jsonDispatcher;

    public void init() {
        jsonDispatcher = new JsonDispatcher();

        // com.imdevil.assistant.RuleRegistry#registerRules()
        try {
            Class<?> ruleRegistryClass = Class.forName("com.imdevil.assistant.RuleRegistry");
            Method registerRulesMethod = ruleRegistryClass.getDeclaredMethod("registerRules", RuleRepository.class);
            registerRulesMethod.setAccessible(true);
            registerRulesMethod.invoke(null, jsonDispatcher.repository);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Assistant addHandler(IAssistantHandler handler) {
        jsonDispatcher.registerHandlerClasses(handler);
        return this;
    }

    public void dispatchJson(String json) {
        jsonDispatcher.dispatch(json);
    }
}
