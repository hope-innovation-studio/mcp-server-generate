package org.hope.mcpservergenerate.controller;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.hope.mcpservergenerate.model.tree.httptemplatenode.TsHttpToolTemplateFileNode;
import org.hope.mcpservergenerate.service.impl.FileServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 关岁安
 * @since 2026/8/12
 */
@RestController
@RequestMapping("/generate")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;

    /**
     * 在系统文件夹中新添加一个文件夹
     * @param parentId 父文件id
     * @param pathName 路径名字
     * @return
     */
    @PostMapping("add-folder")
    public R<FolderNode> addFolder(
            @RequestParam String parentId,
            @RequestParam String pathName
    ){
        return fileService.addFolder(parentId, pathName);
    }

    @PostMapping("add-http-ts-tool-to-folder")
    public R<TsHttpToolTemplateFileNode<TsHttpToolTemplateModel>> addHttpTsToolToFolder(
            @RequestParam String toolName,
            @RequestParam String parentNodeId,
            @RequestParam String toolId
    ){
        return fileService.addHttpTsToolToFolder(toolName,parentNodeId,toolId);
    }

}
