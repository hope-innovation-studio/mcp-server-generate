package org.hope.mcpservergenerate.model.tree.httptemplatenode;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hope.mcpservergenerate.model.tree.FileNode;
import org.hope.mcpservergenerate.model.tree.enums.FileNodeType;

/**
 * @author 关岁安
 * @since 2026/8/12
 */
@Data
@NoArgsConstructor
public class TsHttpStaticTemplateFileNode extends FileNode {

    /**
     * 来源路径
     */
    private String sourcePath;

    public TsHttpStaticTemplateFileNode(String path) {
        super(path);
    }

    public TsHttpStaticTemplateFileNode(String path, String sourcePath){
        super(path);
        this.sourcePath = sourcePath;
    }


    @Override
    public FileNodeType getNodeType() {
        return FileNodeType.STATIC_FILE;
    }

}
