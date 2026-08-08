package org.hope.mcpservergenerate.context;

import org.hope.mcpservergenerate.model.tree.FileSystemNode;

/**
 * @author 关岁安
 * @since 2026/8/8
 * 存放组合模式的根节点
 */
public class HttpFileContext {

    private FileSystemNode root;

    public HttpFileContext(FileSystemNode root) {
        this.root = root;
    }

}