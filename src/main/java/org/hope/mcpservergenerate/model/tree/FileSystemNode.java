package org.hope.mcpservergenerate.model.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hope.mcpservergenerate.model.tree.enums.FileNodeType;

import static org.hope.mcpservergenerate.utils.id.SnowflakeIdGenerator.nextId;

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
     * 确保每一个节点都唯一的
     */
    private String id = nextId();

    /**
     * 路径
     */
    private String path;

    public FileSystemNode(String path){
        this.path = path;
    }

    /**
     * 判断是否为文件夹
     * @return true代表是文件夹 false代表是文件
     */
    public abstract FileNodeType getNodeType();
}