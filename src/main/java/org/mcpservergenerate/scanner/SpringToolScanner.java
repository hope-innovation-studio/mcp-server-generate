package org.mcpservergenerate.scanner;

import org.mcpservergenerate.model.ToolDefinition;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * 专门用来扫描springboot中的类
 * @author 关岁安
 */
public class SpringToolScanner {

    private final ToolScanner toolScanner = new ToolScanner();

    public List<ToolDefinition> scan(ApplicationContext applicationContext) {
        return Arrays.stream(applicationContext.getBeanDefinitionNames())
                .map(applicationContext::getBean)
                .map(this::getTargetClass)
                .flatMap(targetClass -> toolScanner.scan(targetClass).stream())
                .toList();
    }

    private Class<?> getTargetClass(Object bean) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        return targetClass != null ? targetClass : bean.getClass();
    }
}
