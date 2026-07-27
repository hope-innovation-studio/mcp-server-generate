package org.hope.mcpservergenerate.service;

import org.hope.mcpservergenerate.model.ToolDefinition;
import org.hope.mcpservergenerate.model.http.HttpToolDefinition;

import java.util.List;

/**
 * @author 关岁安
 * @since 2026/7/26
 */
public interface IService {

    /**
     * 收集toolDefinition -> 调用转化器转化为HttpToolDefinition -> 返回
     */
    void httpDataPipeline();

}
