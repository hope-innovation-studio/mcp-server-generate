package org.hope.mcpservergenerate.service;

import org.hope.mcpservergenerate.model.ToolDefinition;
import org.hope.mcpservergenerate.model.http.HttpToolDefinition;

import java.util.List;

/**
 * @author 关岁安
 * @since 2026/7/26
 */
public interface IBaseService {

    /**
     * 收集toolDefinition -> 调用转化器转化为HttpToolDefinition -> 返回
     * @return 所有的http的工具定义
     */
    List<HttpToolDefinition> httpDataPipeline();

}
