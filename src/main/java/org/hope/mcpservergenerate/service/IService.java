package org.hope.mcpservergenerate.service;

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
