package org.hope.mcpservergenerate.model.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 * @since 2026/8/8
 */
@Data
@NoArgsConstructor
public class FileNode extends FileSystemNode{

    @Override
    public void execute() {
        System.out.println("执行业务方法");
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public void update() {

    }
}
