package org.hope.mcpservergenerate.scanner;

import org.hope.mcpservergenerate.model.ToolDefinition;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 类级别的
 * 专门用来扫描springboot中的类
 * @author 关岁安
 */
public class SpringToolScanner {

    private final ToolScanner toolScanner = new ToolScanner();

    public List<ToolDefinition> scan(ApplicationContext applicationContext) {
//        Map<String,List<>>
//        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
//            Object bean = applicationContext.getBean(beanDefinitionName);
//            Class<?> targetClass = getTargetClass(bean);
//            List<ToolDefinition> toolDefinitionList = toolScanner.scan(targetClass);
//
//        }
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
