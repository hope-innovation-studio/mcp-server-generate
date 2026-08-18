package org.hope.mcpservergenerate.model.templatemodel.ts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author 关岁安
 * @since 2026/8/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TsHttpToolTemplateModel {
    private String className;
    private String toolName;
    private String requestMethod;
    private String url;
    private String description;
    private Map<String, List<TsHttpToolParameterTemplateModel>> queryTsHttpParameter;
    private List<TsHttpToolParameterTemplateModel> allTsHttpParameter;
}
