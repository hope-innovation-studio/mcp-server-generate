package org.hope.mcpservergenerate.model.vo.preview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 关岁安
 * @since 2026/8/18
 * 供前端预览的实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FilePreviewResponse {
    private String path;
    private String language;
    private String content;
}