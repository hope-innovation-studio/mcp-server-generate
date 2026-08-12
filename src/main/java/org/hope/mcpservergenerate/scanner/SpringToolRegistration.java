package org.hope.mcpservergenerate.scanner;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.converter.Converter;

import org.hope.mcpservergenerate.model.tooldefinition.ToolDefinition;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpToolDefinition;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 Bean 后置处理，将 {@link org.hope.mcpservergenerate.annotation.ExposeMcpTool} 注解类加载
 *
 * @author 杨正
 * @since 2026/8/10 13:33
 */
@RequiredArgsConstructor
public class SpringToolRegistration implements BeanPostProcessor {

    private final ToolScanner toolScanner;

    private final Converter<HttpToolDefinition> converter;

    private final HttpToolDefinitionContext httpToolDefinitionContext;

    private Class<?> getTargetClass(Object bean) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        return targetClass != null ? targetClass : bean.getClass();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        List<ToolDefinition> beanHttpToolDefinitionList = toolScanner.scan(getTargetClass(bean));

        List<HttpToolDefinition> ans = new ArrayList<>();
        //扫描工具
        for (ToolDefinition toolDefinition : beanHttpToolDefinitionList) {
            List<HttpToolDefinition> convert = converter.convert(toolDefinition);
            if (convert != null && !convert.isEmpty()) {
                ans.addAll(convert);
            }
        }
        //扫描http方法
        for (HttpToolDefinition an : ans) {
            System.out.println(an);
            httpToolDefinitionContext.addIfAbsent(an.getId(),an);
        }

        return bean;
    }


}
