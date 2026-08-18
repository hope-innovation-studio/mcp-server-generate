package org.hope.mcpservergenerate.service.impl;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.converter.impl.TsHttpToolTemplateModelConverter;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpToolDefinition;
import org.hope.mcpservergenerate.model.tree.FileSystemNode;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.hope.mcpservergenerate.model.tree.httptemplatenode.TsHttpToolTemplateFileNode;

import static org.hope.mcpservergenerate.constant.HttpConfigConstant.BASE_URL;
import static org.hope.mcpservergenerate.constant.HttpGenerateFileConstant.REQUEST_FILE_PATH;

/**
 * @author 关岁安
 * @since 2026/8/12
 */
@RequiredArgsConstructor
public class FileServiceImpl {

    private final HttpFileTreeContext httpFileTreeContext;

    private final TreeServiceImpl treeService;

    private final TsHttpToolTemplateModelConverter modelConverter;

    private final HttpToolDefinitionContext httpToolDefinitionContext;


    public R<FolderNode> addFolder(String parentId, String pathName) {
        if (parentId == null || parentId.isBlank()) {
            return R.fail(400, "父文件夹 ID 不能为空");
        }
        if (pathName == null || pathName.isBlank()) {
            return R.fail(400, "文件夹名称不能为空");
        }

        String folderName = pathName.trim();
        FileSystemNode root = httpFileTreeContext.getRoot();
        FileSystemNode parentNode = treeService.findById(parentId, root);
        if (parentNode == null) {
            return R.fail(404, "父节点不存在");
        }
        if (!(parentNode instanceof FolderNode parentFolder)) {
            return R.fail(400, "不能在文件节点下创建文件夹");
        }
        if (treeService.hasChildWithName(parentFolder, folderName)) {
            return R.fail(409, "同级目录下已存在同名节点");
        }

        FolderNode newFolder = (FolderNode) parentFolder.add(parentNode,folderName);
        return R.success(newFolder);
    }


    /**
     * TODO 这个地方是不是可以解耦？
     * 转化器这边
     * @param toolName
     * @param parentNodeId
     * @param toolId
     * @return
     */
    public R<TsHttpToolTemplateFileNode<TsHttpToolTemplateModel>> addHttpTsToolToFolder(String className,
                                                                                        String toolName,
                                                                                         String parentNodeId,
                                                                                         String toolId) {
        if (toolName == null || toolName.isBlank()) {
            return R.fail(400, "Tool 名称不能为空");
        }
        if (parentNodeId == null || parentNodeId.isBlank()) {
            return R.fail(400, "父文件夹 ID 不能为空");
        }
        if (toolId == null || toolId.isBlank()) {
            return R.fail(400, "Tool ID 不能为空");
        }

        HttpToolDefinition httpToolDefinition = httpToolDefinitionContext.get().get(toolId);
        if (httpToolDefinition == null) {
            return R.fail(404, "Tool 不存在");
        }

        FileSystemNode parentNode = treeService.findById(parentNodeId);
        if (!(parentNode instanceof FolderNode parentFolder)) {
            return R.fail(400, "目标节点不是文件夹");
        }

        String normalizedToolName = toolName.trim();
        /**
         * TODO 是否应该在这里添加硬编码结构
         */
        String fileName = normalizedToolName + ".ts";
        if (treeService.hasChildWithName(parentFolder, fileName)) {
            return R.fail(409, "同级目录下已存在同名文件");
        }

        TsHttpToolTemplateModel templateModel = modelConverter.convert(
                className,
                normalizedToolName,
                httpToolDefinition,
                BASE_URL
        );

        String filePath = treeService.buildChildPath(
                parentFolder.getPath(),
                fileName
        );

        TsHttpToolTemplateFileNode<TsHttpToolTemplateModel> fileNode =
                new TsHttpToolTemplateFileNode<>(
                        filePath,
                        REQUEST_FILE_PATH,
                        templateModel
                );

        parentFolder.add(fileNode);
        return R.success(fileNode);

    }
}
