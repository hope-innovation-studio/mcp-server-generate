package org.hope.mcpservergenerate.context;

import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpToolDefinition;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 关岁安
 * @since 2026/7/26
 */
@Component
public class HttpToolDefinitionContext {
    private Map<String, HttpToolDefinition> items;

    public Map<String, HttpToolDefinition> get(){
        return this.items;
    }

    public void set(Map<String, HttpToolDefinition> map){
        this.items = map;
    }
}
