package org.hope.mcpservergenerate.templateModel.ts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 关岁安
 * @since 20268/8/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TsHttpToolParameterTemplateModel {
    private String key;

    // "PATH"、"PARAM"、"HEADER"、"BODY"
    private String location;

    private boolean required;

    // 例如 z.string()、z.number().int()、z.array(z.string())
    private String zodSchema;

    private String description;
}