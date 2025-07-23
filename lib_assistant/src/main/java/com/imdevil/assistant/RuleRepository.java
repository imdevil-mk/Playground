package com.imdevil.assistant;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

class RuleRepository {
    private static final String TAG = "RuleRepository";

    private final List<Rule> rules = new ArrayList<>();
    // 添加字段级别的索引，相同的字段，由于值的不同，可以对应到多个规则上
    private final Map<String, List<Rule>> fieldIndex = new HashMap<>();
    // 添加方法级别的索引  一个方法可以对应到多个规则上
    private final Map<String, List<Rule>> methodIndex = new HashMap<>();
    // methodKey(className#methodName) -- invoke
    public final Map<String, Function<String, Void>> handlerCache = new ConcurrentHashMap<>();
    // IAssistantHandler handler class name --  it`s annotated method name
    public final Map<String, Set<String>> handlerMethodIndex = new HashMap<>();

    public void addRule(Rule rule) {
        rules.add(rule);

        // 更新字段索引
        for (String field : rule.getAllFields()) {
            fieldIndex.computeIfAbsent(field, k -> new ArrayList<>()).add(rule);
        }

        // 更新方法索引
        methodIndex.computeIfAbsent(rule.getMethodKey(), k -> new ArrayList<>()).add(rule);

        handlerMethodIndex
                .computeIfAbsent(rule.getHandlerClassName(), k -> new HashSet<>())
                .add(rule.getHandlerMethodName());

        sortRules();
    }

    private void sortRules() {
        // 按优先级和特化度排序
        rules.sort(Rule.RULE_COMPARATOR);
    }

    // 按方法查询规则
    public List<Rule> getRulesForMethod(String methodKey) {
        return methodIndex.getOrDefault(methodKey, Collections.emptyList());
    }

    public Function<String, Void> getHandler(String methodKey) {
        return handlerCache.get(methodKey);
    }

    // 优化匹配逻辑
    public Optional<Rule> findBestMatch(JsonObject json) {
        Set<Rule> candidates = new HashSet<>();

        // 1. 通过字段索引快速筛选
        for (String field : json.keySet()) {
            List<Rule> fieldRules = fieldIndex.get(field);
            if (fieldRules != null) {
                candidates.addAll(fieldRules);
            }
        }

        // 2. 如果没有候选，考虑所有规则
        if (candidates.isEmpty()) {
            candidates = new HashSet<>(rules);
        }

        // 3. 按优先级排序候选规则
        List<Rule> sortedCandidates = new ArrayList<>(candidates);
        sortedCandidates.sort(Rule.RULE_COMPARATOR);

        // 4. 找到第一个匹配的规则
        for (Rule rule : sortedCandidates) {
            if (rule.matches(json)) {
                return Optional.of(rule);
            }
        }

        return Optional.empty();
    }
}
