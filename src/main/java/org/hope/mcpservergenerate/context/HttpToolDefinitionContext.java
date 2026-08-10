package org.hope.mcpservergenerate.context;

import org.hope.mcpservergenerate.model.http.HttpToolDefinition;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 关岁安
 * @since 2026/7/26
 */
@Component
public class HttpToolDefinitionContext {
    private Map<String, HttpToolDefinition> items = new ConcurrentHashMap<>();

    public Map<String, HttpToolDefinition> get(){
        return this.items;
    }

    public void set(Map<String, HttpToolDefinition> map){
        this.items = map;
    }

    public HttpToolDefinition addIfAbsent(String k,HttpToolDefinition v){
        return items.putIfAbsent(k,v);
    }
}
