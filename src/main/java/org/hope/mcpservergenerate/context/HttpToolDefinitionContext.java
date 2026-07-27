package org.hope.mcpservergenerate.context;

import org.hope.mcpservergenerate.model.http.HttpToolDefinition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 关岁安
 * @since 2026/7/26
 */
@Component
public class HttpToolDefinitionContext {
    private List<HttpToolDefinition> httpToolDefinitions = new ArrayList<>();

    public void set(List<HttpToolDefinition> list){
        this.httpToolDefinitions = list;
    }

    public List<HttpToolDefinition> get(){
        return this.httpToolDefinitions;
    }
}
