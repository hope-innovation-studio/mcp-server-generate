package org.hope.mcpservergenerate.converter;

import org.hope.mcpservergenerate.model.ToolDefinition;

import java.util.List;

/**
 * @author 关岁安
 * @since 2026/7/21
 *
 * 将 ToolDefinition -> 任意ToolDefinition
 */
public interface Converter<T> {
    List<T> convert(ToolDefinition toolDefinition);
}
