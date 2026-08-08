package org.hope.mcpservergenerate.model.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 关岁安
 * @since 2026/8/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderNode extends FileSystemNode{

    public List<FileSystemNode> children = new ArrayList<>();

    @Override
    public void execute() {

    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public void update() {

    }

    public void add(){

    }

    public void delete(){

    }

    public void select(){

    }



}
