package org.hope.mcpservergenerate.autoconfigure;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author 关岁安
 * @since 2026/7/27
 * 从yaml文件获取到数据
 */
@ConfigurationProperties(prefix = "mcp.generator")
@Data
public class McpGeneratorProperties {

    /**
     * 是否启用 MCP Generator 寄宿者组件，默认开启
     */
    private boolean enabled = true;

    /**
     * 寄宿者 UI 界面的访问路径前缀（举例：支持被寄宿者自定义）
     */
    private String path = "/mcp-ui";


}
