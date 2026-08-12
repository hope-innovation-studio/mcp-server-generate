package org.hope.mcpservergenerate.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hope.mcpservergenerate.model.tree.FileSystemNode;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.springframework.stereotype.Component;

/**
 * @author 关岁安
 * @since 2026/8/8
 * 存放组合模式的根节点
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpFileTreeContext {

    private FileSystemNode root = new FolderNode("default");

}