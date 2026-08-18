package org.hope.mcpservergenerate.model.dto.preview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;

/**
 * @author 关岁安
 * @since 2026/8/18
 * 修改预览代码所需要的实体数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PreviewHttpTsTemplate {
    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 模板信息
     */
    private TsHttpToolTemplateModel tsHttpToolTemplateModel;
}
