package org.hope.mcpservergenerate.service.impl;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.tree.FileSystemNode;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.hope.mcpservergenerate.model.tree.enums.FileNodeType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 关岁安
 * @since 2026/8/12
 */
@RequiredArgsConstructor
public class FileServiceImpl {

    private final HttpFileTreeContext httpFileTreeContext;

    public R<String> addFolder(String parentId, String pathName) {
        FileSystemNode root = httpFileTreeContext.getRoot();
        FileSystemNode parentNode = findById(parentId, root);
        if(parentNode == null){
            //如果为空
            return R.fail(403,"传入的id有问题");
        }else{
            if(parentNode.getNodeType() == FileNodeType.FOLDER){
                FileSystemNode newFolder = new FolderNode(pathName, new ArrayList<FileSystemNode>());
                FolderNode folderNode = (FolderNode) parentNode;
                folderNode.add(newFolder);
                return R.success("创建成功");
            }else{
                return R.fail(403,"不能在文件下创建文件夹");
            }
        }

    }


    private FileSystemNode findById(String parentId, FileSystemNode node){
        if(node == null){
            return null;
        }
        if(node.getId().equals(parentId)){
            return node;
        }
        if(node.getNodeType() == FileNodeType.FOLDER){
            List<FileSystemNode> children = ((FolderNode) node).getChildren();
            for (FileSystemNode child : children) {
                FileSystemNode byId = findById(parentId, child);
                if(byId != null){
                    return byId;
                }
            }
        }
        return null;
    }

}
