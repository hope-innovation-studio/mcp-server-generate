package org.mcpservergenerate.converter.impl;

import org.mcpservergenerate.converter.Converter;
import org.mcpservergenerate.model.HttpParameterDefinition;
import org.mcpservergenerate.model.HttpToolDefinition;
import org.mcpservergenerate.model.ToolDefinition;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.mcpservergenerate.utils.http.HttpMethodUtils.getHttpMethods;
import static org.mcpservergenerate.utils.http.HttpParamUtils.getHttpParam;
import static org.mcpservergenerate.utils.http.RequestPathUtils.getRequestPath;

/**
 * @author 关岁安
 * @since 2026/7/21
 * 将ToolDefinition -> HttpToolDefinition
 */
public class HttpToolDefinitionConverter implements Converter<HttpToolDefinition> {


    /**
     * 将toolDefinition -> HttpToolDefinition
     * @param toolDefinition
     * @return 由于不同的路径和请求方式会形成多种组合 一种组合就是一种tool
     */
    @Override
    public List<HttpToolDefinition> convert(ToolDefinition toolDefinition) {
        /**
         * TODO 需要补充异常处理和健壮性处理
         */
        Method method = toolDefinition.getMethod();
        Type returnType = method.getGenericReturnType();
        //获取参数列表
        List<HttpParameterDefinition> httpParamList = getHttpParam(method);
        List<HttpToolDefinition> ans = new ArrayList<>();
        //获取所有的路径
        List<String> allPaths = getRequestPath(toolDefinition.getMethod());
        //获取所有的请求方式
        List<HttpMethod> allHttpMethods = getHttpMethods(toolDefinition.getMethod());
        //开始组合
        for (String path : allPaths) {
            for (HttpMethod httpMethod : allHttpMethods) {
                HttpToolDefinition httpToolDefinition = new HttpToolDefinition(toolDefinition);
                httpToolDefinition
                        .setEndpoint(path)
                        .setRequestMethod(httpMethod)
                        .setParameters(httpParamList)
                        .setReturnType(returnType);
                ans.add(httpToolDefinition);
            }
        }
        return ans;
    }

}
