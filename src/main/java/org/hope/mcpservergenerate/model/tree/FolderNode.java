package org.hope.mcpservergenerate.model.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hope.mcpservergenerate.model.tree.enums.FileNodeType;

import java.util.ArrayList;
import java.util.List;

import static org.hope.mcpservergenerate.utils.id.SnowflakeIdGenerator.nextId;

/**
 * @author 关岁安
 * @since 2026/8/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderNode extends FileSystemNode{

    public List<FileSystemNode> children = new ArrayList<>();

    public FolderNode(String path){
        super(path);
    }

    public FolderNode(String path, List<FileSystemNode> children){
        super(path);
        this.children = children;
    }

    @Override
    public FileNodeType getNodeType() {
        return FileNodeType.FOLDER;
    }



    public void add(FileSystemNode node) {
        children.add(node);
    }

    public void delete(){

    }

    public void select(){

    }



}
