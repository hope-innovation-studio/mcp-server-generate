package org.hope.mcpservergenerate.model.tree.httptemplatenode;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;
import org.hope.mcpservergenerate.model.tree.FileSystemNode;

/**
 * @author 关岁安
 * @since 2026/8/8
 */

@Data
@AllArgsConstructor
public class TsHttpToolTemplateFileNode extends FileSystemNode {

    private TsHttpToolTemplateModel toolTemplateModel;

    public TsHttpToolTemplateFileNode(
            String path,
            TsHttpToolTemplateModel toolTemplateModel
    ) {
        super(path);
        this.toolTemplateModel = toolTemplateModel;
    }

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
}
