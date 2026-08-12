package org.hope.mcpservergenerate.model.tree;

import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hope.mcpservergenerate.model.tree.enums.FileNodeType;

import java.util.Map;

/**
 * @author 关岁安
 * @since 2026/8/8
 */
@Data
@NoArgsConstructor
public abstract class FileNode extends FileSystemNode{

    /**
     * 扩展参数
     */
    private Map<String,Object> extendParameters;

    public FileNode(String path) {
        super(path);
    }


    @Override
    public FileNodeType getNodeType() {
        return FileNodeType.DEFAULT;
    }

}
