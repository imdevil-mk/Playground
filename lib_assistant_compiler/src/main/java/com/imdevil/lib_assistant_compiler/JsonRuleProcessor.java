package com.imdevil.lib_assistant_compiler;

import com.imdevil.assistant.JsonRuleConfig;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class JsonRuleProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(JsonRuleConfig.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) return false;

        // 收集所有规则信息
        Map<String, List<RuleInfo>> rulesByClass = new HashMap<>();
        Elements elementUtils = processingEnv.getElementUtils();

        for (Element element : roundEnv.getElementsAnnotatedWith(JsonRuleConfig.class)) {
            if (element.getKind() != ElementKind.METHOD) continue;

            ExecutableElement method = (ExecutableElement) element;
            TypeElement classElement = (TypeElement) method.getEnclosingElement();
            String className = classElement.getQualifiedName().toString();
            String methodName = method.getSimpleName().toString();

            JsonRuleConfig config = method.getAnnotation(JsonRuleConfig.class);
            List<RuleInfo> rules = new ArrayList<>();

            for (JsonRuleConfig.Rule ruleDef : config.value()) {
                RuleInfo rule = new RuleInfo();
                rule.className = className;
                rule.methodName = methodName;
                rule.ruleName = ruleDef.name();
                rule.fields = ruleDef.fields();
                rule.priority = ruleDef.priority();

                rules.add(rule);
            }

            rulesByClass.computeIfAbsent(className, k -> new ArrayList<>()).addAll(rules);
        }

        // 生成注册类
        generateRegistryClass(rulesByClass);
        return true;
    }

    private void generateRegistryClass(Map<String, List<RuleInfo>> rulesByClass) {
        try {
            String packageName = "com.imdevil.assistant";
            String className = "RuleRegistry";
            JavaFileObject file = processingEnv.getFiler().createSourceFile(packageName + "." + className);

            try (Writer writer = file.openWriter()) {
                writer.write("package " + packageName + ";\n\n");
                writer.write("import com.imdevil.assistant.RuleRepository;\n");
                writer.write("import com.imdevil.assistant.Rule;\n");
                writer.write("import java.util.*;\n\n");

                writer.write("public class " + className + " {\n");
                writer.write("    public static void registerRules(RuleRepository repository) {\n");

                for (Map.Entry<String, List<RuleInfo>> entry : rulesByClass.entrySet()) {
                    String handlerClass = entry.getKey();
                    List<RuleInfo> rules = entry.getValue();

                    writer.write("        // Rules for " + handlerClass + "\n");

                    for (RuleInfo rule : rules) {
                        writer.write("        repository.addRule(Rule.createRule(\n");
                        writer.write("            \"" + rule.ruleName + "\",\n");
                        writer.write("            \"" + rule.fields + "\",\n");
                        writer.write("            " + rule.priority + ",\n");
                        writer.write("            \"" + handlerClass + "\",\n");
                        writer.write("            \"" + rule.methodName + "\"));\n");
                    }
                }

                writer.write("    }\n");
                writer.write("}\n");
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR, "Failed to generate RuleRegistry: " + e.getMessage());
        }
    }

    private static class RuleInfo {
        String className;
        String methodName;
        String ruleName;
        String fields;
        int priority;
    }
}