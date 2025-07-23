package com.imdevil.assistant;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) // 仅保留在源码级别
@Target(ElementType.METHOD)
public @interface JsonRuleConfig {
    @interface Rule {
        String name() default "";

        // 字段匹配条件 (格式: "字段名=值1,值2;字段名=值3,值4")
        String fields() default "";

        int priority() default 0;
    }

    Rule[] value();
}
