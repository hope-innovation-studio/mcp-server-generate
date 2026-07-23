package org.hope.mcpservergenerate.scanner;

import org.hope.mcpservergenerate.annotation.ExposeMcpTool;
import org.hope.mcpservergenerate.model.ToolDefinition;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * 扫描一个类中有哪些方法打上了 @ExposeMcpTool
 * 并且放回ToolDefinition类型的集合
 * 一个类中有多个方法
 * @author 关岁安
 * @since 2026/7/21
 */
public class ToolScanner {

    public List<ToolDefinition> scan(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.isAnnotationPresent(ExposeMcpTool.class))
                .map(this::toToolDefinition)
                .toList();
    }

    private ToolDefinition toToolDefinition(Method method) {
        ExposeMcpTool annotation = method.getAnnotation(ExposeMcpTool.class);
        String name = annotation.name().isBlank() ? method.getName() : annotation.name();
        return new ToolDefinition(name, annotation.description(), method);
    }
}
