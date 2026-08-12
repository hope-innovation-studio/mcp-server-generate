package org.hope.mcpservergenerate.model.tree.httptemplatenode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;
import org.hope.mcpservergenerate.model.tree.FileNode;
import org.hope.mcpservergenerate.model.tree.enums.FileNodeType;

import java.util.Map;


/**
 * @author 关岁安
 * @since 2026/8/8
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsHttpToolTemplateFileNode<T> extends FileNode {

    /**
     * 模板路径
     */
    private String templatePath;

    private T toolTemplateModel;

    public TsHttpToolTemplateFileNode(String path,String templatePath,T toolTemplateModel) {
        super(path);
        this.toolTemplateModel = toolTemplateModel;
        this.templatePath = templatePath;
    }

    @Override
    public FileNodeType getNodeType() {
        return FileNodeType.TEMPLATE_FILE;
    }
}
