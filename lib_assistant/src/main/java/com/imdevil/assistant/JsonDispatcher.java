package com.imdevil.assistant;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

class JsonDispatcher {
    private static final String TAG = "JsonDispatcher";
    public final RuleRepository repository = new RuleRepository();

    public JsonDispatcher() {

    }

    public void registerHandlerClasses(IAssistantHandler handler) {
        Class<?> handlerClass = handler.getClass();
        String className = handlerClass.getName();
        Set<String> annotatedMethods = repository.handlerMethodIndex.get(className);
        if (annotatedMethods.isEmpty()) return;
        for (String annotatedMethod : annotatedMethods) {
            String methodKey = className + "#" + annotatedMethod;
            try {
                Method method = handlerClass.getMethod(annotatedMethod, String.class);
                repository.handlerCache.put(methodKey, s -> {
                    invokeHandlerMethod(methodKey, handler, method, s);
                    return null;
                });
                //Log.d(TAG, "registerHandlerClasses: " + methodKey);
                System.out.println("JsonDispatcher registerHandlerClasses: " + methodKey);
            } catch (NoSuchMethodException e) {
                //Log.d(TAG, "registerHandlerClasses: " + methodKey + " with no String args !!");
                System.out.println("JsonDispatcher registerHandlerClasses: " + methodKey + " with no String args !!");
            }
        }
    }

    private void invokeHandlerMethod(String methodKey, Object handler, Method method, String json) {
        try {
            method.invoke(handler, json);
        } catch (Exception e) {
            //Log.d(TAG, "invokeHandlerMethod: " + methodKey + " with Exception " + e);
            System.out.println("JsonDispatcher invokeHandlerMethod: " + methodKey + " with Exception " + e);
        }
    }

    public void dispatch(String jsonInput) {
        JsonObject json = JsonParser.parseString(jsonInput).getAsJsonObject();
        Optional<Rule> matchedRule = repository.findBestMatch(json);
        matchedRule.ifPresent(rule -> {
            Function<String, Void> handler = repository.getHandler(rule.getMethodKey());
            if (handler != null) {
                handler.apply(jsonInput);
            }
        });
    }
}