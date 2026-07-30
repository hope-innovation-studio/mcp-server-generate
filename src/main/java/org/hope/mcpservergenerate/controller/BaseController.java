package org.hope.mcpservergenerate.controller;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.model.http.HttpToolDefinition;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 关岁安
 * @since 2026/7/27
 * 针对HttpToolDefinition的控制器
 */
@RestController
@RequestMapping("/httpDefinitionTool")
@RequiredArgsConstructor
public class BaseController {

    private final HttpToolDefinitionContext httpToolDefinitionContext;

    @GetMapping("get")
    public Map<String,HttpToolDefinition> get(){
        return httpToolDefinitionContext.get();
    }


    @PostMapping("post")
    public void update(){

    }

}
