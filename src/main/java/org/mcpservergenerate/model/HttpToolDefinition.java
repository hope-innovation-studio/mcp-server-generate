package org.mcpservergenerate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.List;


/**
 * 带有http信息的工具定义
 * 给代码生成器参数
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class HttpToolDefinition extends ToolDefinition{
    /**
     * 路径
     */
    private String endpoint;
    /**
     * 请求方式
     */
    private HttpMethod requestMethod;
    /**
     * 请求体格式，
     * 例如 application/json、application/x-www-form-urlencoded、multipart/form-data。
     */
    private String consumes;
    /**
     * 响应格式，通常是 application/json
     */
    private String produces;
    /**
     * 请求参数
     */
    private List<HttpParameterDefinition> parameters;
    /**
     * 返回类型
     */
    private Type returnType;

    public HttpToolDefinition(ToolDefinition toolDefinition){
        super(toolDefinition.getName(),
                toolDefinition.getDescription(),
                toolDefinition.getMethod());
    }
}
