package com.imdevil.assistant;


import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Rule {
    private final String name;
    private final Map<String, Set<String>> fieldConditions; // 字段名 -> 允许的值集合
    private final Set<String> requiredAnyValueFields;      // 必须存在但值任意的字段
    private final Set<String> optionalAnyValueFields;      // 可选存在且值任意的字段
    private final String handlerClassName;
    private final String handlerMethodName;
    private final int priority;                            // 显式优先级
    private final int specificity;                         // 规则特化度

    public Rule(String name, Map<String, Set<String>> fieldConditions,
                Set<String> requiredAnyValueFields, Set<String> optionalAnyValueFields,
                int priority, String handlerClassName, String handlerMethodName) {
        this.name = name;
        this.fieldConditions = Collections.unmodifiableMap(fieldConditions);
        this.requiredAnyValueFields = Collections.unmodifiableSet(requiredAnyValueFields);
        this.optionalAnyValueFields = Collections.unmodifiableSet(optionalAnyValueFields);
        this.priority = priority;
        this.specificity = calculateSpecificity();
        this.handlerClassName = handlerClassName;
        this.handlerMethodName = handlerMethodName;
    }

    private int calculateSpecificity() {
        int score = 0;

        // 1. 每个带值条件的字段加10分
        score += fieldConditions.size() * 10;

        // 2. 每个必须存在但值任意的字段加5分
        score += requiredAnyValueFields.size() * 5;

        // 3. 每个字段的条件值数量越少，得分越高
        for (Set<String> values : fieldConditions.values()) {
            if (!values.isEmpty()) {
                score += (10 - Math.min(values.size(), 10));
            }
        }

        return score;
    }

    public Set<String> getAllFields() {
        Set<String> allFields = new HashSet<>();
        allFields.addAll(fieldConditions.keySet());
        allFields.addAll(requiredAnyValueFields);
        allFields.addAll(optionalAnyValueFields);
        return allFields;
    }

    public boolean matches(JsonObject json) {
        // 检查带值条件的字段
        for (Map.Entry<String, Set<String>> entry : fieldConditions.entrySet()) {
            String field = entry.getKey();
            Set<String> allowedValues = entry.getValue();

            if (!json.has(field)) {
                return false; // 字段不存在
            }

            String actualValue = json.get(field).getAsString();
            if (!allowedValues.contains(actualValue)) {
                return false; // 值不匹配
            }
        }

        // 检查必须存在但值任意的字段
        for (String field : requiredAnyValueFields) {
            if (!json.has(field)) {
                return false; // 字段不存在
            }
            // 值任意，不检查具体值
        }

        // 检查可选存在且值任意的字段
        for (String field : optionalAnyValueFields) {
            if (json.has(field)) {
                // 字段存在，值任意，不检查具体值
            }
            // 字段不存在也可以接受
        }

        return true;
    }

    public String getMethodKey() {
        return handlerClassName + "#" + handlerMethodName;
    }

    public String getHandlerClassName() {
        return handlerClassName;
    }

    public String getHandlerMethodName() {
        return handlerMethodName;
    }

    // 规则排序：先按显式优先级（高优先），再按特化度（高优先）
    public static final Comparator<Rule> RULE_COMPARATOR = (r1, r2) -> {
        // 1. 显式优先级比较（数字越大优先级越高）
        int priorityCompare = Integer.compare(r2.priority, r1.priority);
        if (priorityCompare != 0) return priorityCompare;

        // 2. 特化度比较（越具体优先级越高）
        return Integer.compare(r2.specificity, r1.specificity);
    };

    public static Rule createRule(
            String name,
            String fieldsConfig,
            int priority,
            String className,
            String methodName) {

        Map<String, Set<String>> fieldConditions = new HashMap<>();
        Set<String> requiredAnyValueFields = new HashSet<>();
        Set<String> optionalAnyValueFields = new HashSet<>();

        if (!fieldsConfig.isEmpty()) {
            String[] fieldGroups = fieldsConfig.split(";");
            for (String group : fieldGroups) {
                String[] parts = group.split("=");
                if (parts.length != 2) continue;

                String field = parts[0].trim();
                String[] values = parts[1].split(",");

                if (values.length == 1 && "*".equals(values[0])) {
                    requiredAnyValueFields.add(field.trim());
                } else if (values.length == 1 && "#".equals(values[0])) {
                    optionalAnyValueFields.add(field.trim());
                } else {
                    Set<String> valueSet = new HashSet<>(Arrays.asList(values));
                    fieldConditions.put(field, valueSet);
                }
            }
        }

        return new Rule(name, fieldConditions, requiredAnyValueFields, optionalAnyValueFields,
                priority, className, methodName);
    }
}
