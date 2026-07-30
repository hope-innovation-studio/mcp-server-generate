package org.hope.mcpservergenerate;


import io.swagger.v3.oas.models.Operation;
import org.hope.mcpservergenerate.annotation.ExposeMcpTool;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * @author 关岁安
 * @since 20267/30
 */
@AutoConfiguration
public class McpGeneratorAutoConfiguration {


    /**
     * spring-doc会根据这个生成一个条件查询
     * 查询带有ExposeMcpTool注解的接口
     * 然后专门放到一个以mcp-tools为组文档列表下
     */
    @Bean
    public GroupedOpenApi mcpToolsOpenApi() {
        return GroupedOpenApi.builder()
                .group("mcp-tools")
                .pathsToMatch("/**")
                .addOpenApiMethodFilter(method ->
                        method.isAnnotationPresent(ExposeMcpTool.class)
                )
                //一个Operation指的是一个HTTP 方法级别的接口
                //customizer是定制器的意思
                .addOperationCustomizer(this::customizeMcpOperation)
                .build();
    }


    /**
     *
     * @param operation 操作定义
     * @param handlerMethod 请求接口
     * @return
     */
    private Operation customizeMcpOperation(
            Operation operation,
            HandlerMethod handlerMethod
    ) {
        ExposeMcpTool annotation =
                handlerMethod.getMethodAnnotation(ExposeMcpTool.class);
        if (annotation == null) {
            return operation;
        }
        String toolName = StringUtils.hasText(annotation.name()) ? annotation.name() : handlerMethod.getMethod().getName();
        String description = StringUtils.hasText(annotation.description()) ? annotation.description() : handlerMethod.getMethod().getName();
        // 明确保存 MCP 专用信息，供前端和后续生成器读取
        operation.addExtension("x-mcp-tool-name", toolName);
        operation.addExtension(
                "x-mcp-tool-description",
                description
        );
        return operation;
    }
}