package org.hope.mcpservergenerate.service.impl;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.model.tree.FileSystemNode;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 关岁安
 * 树的所有的操作
 */
@Component
@RequiredArgsConstructor
public class TreeServiceImpl {

    private final HttpFileTreeContext httpFileTreeContext;



    public boolean hasChildWithName(FolderNode parent, String nodeName) {
        for (FileSystemNode child : parent.getChildren()) {
            if (nodeName.equals(getNodeName(child))) {
                return true;
            }
        }
        return false;
    }

    public String buildChildPath(String parentPath, String childName) {
        if (parentPath == null || parentPath.isBlank()) {
            return childName;
        }
        return parentPath.endsWith("/")
                ? parentPath + childName
                : parentPath + "/" + childName;
    }

    public String getNodeName(FileSystemNode node) {
        String path = node.getPath();
        if (path == null || path.isBlank()) {
            return "";
        }
        int separatorIndex = path.lastIndexOf('/');
        return separatorIndex >= 0 ? path.substring(separatorIndex + 1) : path;
    }


    public FileSystemNode findById(String nodeId){
        FileSystemNode root = httpFileTreeContext.getRoot();
        return findById(nodeId, root);
    }

    /**
     * 递归寻找有效节点
     * @param nodeId
     * @param node
     * @return
     */
    public FileSystemNode findById(String nodeId, FileSystemNode node){
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
