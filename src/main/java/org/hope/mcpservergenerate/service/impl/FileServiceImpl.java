package org.hope.mcpservergenerate.service.impl;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.tree.FileSystemNode;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import java.util.List;

/**
 * @author 关岁安
 * @since 2026/8/12
 */
@RequiredArgsConstructor
public class FileServiceImpl {

    private final HttpFileTreeContext httpFileTreeContext;

    public R<FolderNode> addFolder(String parentId, String pathName) {
        if (parentId == null || parentId.isBlank()) {
            return R.fail(400, "父文件夹 ID 不能为空");
        }
        if (pathName == null || pathName.isBlank()) {
            return R.fail(400, "文件夹名称不能为空");
        }

        String folderName = pathName.trim();
        FileSystemNode root = httpFileTreeContext.getRoot();
        FileSystemNode parentNode = findById(parentId, root);
        if (parentNode == null) {
            return R.fail(404, "父节点不存在");
        }
        if (!(parentNode instanceof FolderNode parentFolder)) {
            return R.fail(400, "不能在文件节点下创建文件夹");
        }
        if (hasChildWithName(parentFolder, folderName)) {
            return R.fail(409, "同级目录下已存在同名节点");
        }

        FolderNode newFolder = (FolderNode) parentFolder.add(parentNode,folderName);
        return R.success(newFolder);
    }

    private boolean hasChildWithName(FolderNode parent, String nodeName) {
        for (FileSystemNode child : parent.getChildren()) {
            if (nodeName.equals(getNodeName(child))) {
                return true;
            }
        }
        return false;
    }

    private String buildChildPath(String parentPath, String childName) {
        if (parentPath == null || parentPath.isBlank()) {
            return childName;
        }
        return parentPath.endsWith("/")
                ? parentPath + childName
                : parentPath + "/" + childName;
    }

    private String getNodeName(FileSystemNode node) {
        String path = node.getPath();
        if (path == null || path.isBlank()) {
            return "";
        }
        int separatorIndex = path.lastIndexOf('/');
        return separatorIndex >= 0 ? path.substring(separatorIndex + 1) : path;
    }


    /**
     * 递归寻找有效节点
     * @param nodeId
     * @param node
     * @return
     */
    private FileSystemNode findById(String nodeId, FileSystemNode node){
        if(node == null){
            return null;
        }
        if(node.getId().equals(nodeId)){
            return node;
        }
        if(node instanceof FolderNode folderNode){
            List<FileSystemNode> children = folderNode.getChildren();
            for (FileSystemNode child : children) {
                FileSystemNode byId = findById(nodeId, child);
                if(byId != null){
                    return byId;
                }
            }
        }
        return null;
    }

}
