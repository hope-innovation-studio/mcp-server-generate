package org.hope.mcpservergenerate.model.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 * @since 2026/8/8
 * 组合模式抽象基础节点
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class FileSystemNode {

    /**
     * 路径
     */
    private String path;

    public abstract void execute();

    /**
     * 判断是否为文件夹
     * @return true代表是文件夹 false代表是文件
     */
    public abstract boolean isDirectory();

    public abstract void update();

}
