package org.hope.mcpservergenerate.controller;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpToolDefinition;

import org.hope.mcpservergenerate.model.tree.FileSystemNode;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    private final HttpFileTreeContext httpFileTreeContext;

    @GetMapping("get-tool-list")
    public Map<String,HttpToolDefinition> get(){
        return httpToolDefinitionContext.get();
    }

    @GetMapping("get-file-tree")
    public R<FolderNode> getFileTree(){
        return R.success((FolderNode) httpFileTreeContext.getRoot());
    }

}
