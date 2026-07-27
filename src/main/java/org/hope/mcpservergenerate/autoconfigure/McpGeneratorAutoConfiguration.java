package org.hope.mcpservergenerate.autoconfigure;

import org.hope.mcpservergenerate.converter.impl.HttpToolDefinitionConverter;
import org.hope.mcpservergenerate.scanner.SpringToolScanner;
import org.hope.mcpservergenerate.service.IBaseService;
import org.hope.mcpservergenerate.service.impl.IBaseServiceImpl;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author 关岁安
 * @since 2026/7/27
 * AutoConfiguration告诉 Spring Boot “我是一个 Starter 的自动配置类”
 * EnableConfigurationProperties将被寄宿者写在 application.yml 里的配置项，自动注入并绑定到一个 Java 对象中
 * ConditionalOnWebApplication用来判断是一个web项目才能使用这个依赖
 * ConditionalOnProperty根据配置文件中的某个特定参数，决定是否启动寄宿者。
 *
 * 功能：
 * 在被寄宿者启动时，把寄宿者需要的所有 Bean 注册到同一个 Spring 容器中；它不写扫描逻辑本身。
 */

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(McpGeneratorProperties.class)
@ConditionalOnProperty(
        prefix = "mcp.generator",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class McpGeneratorAutoConfiguration {




    @Bean
    public IBaseService baseService(ApplicationContext applicationContext) {
        return new IBaseServiceImpl(
                new SpringToolScanner(),
                applicationContext,
                new HttpToolDefinitionConverter()
        );
    }

    @Bean
    public ApplicationRunner mcpToolInitializer(IBaseService baseService) {
        return args -> baseService.httpDataPipeline();
    }

}
