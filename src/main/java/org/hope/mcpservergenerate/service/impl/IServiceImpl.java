package org.hope.mcpservergenerate.service.impl;


import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.converter.Converter;
import org.hope.mcpservergenerate.model.ToolDefinition;

import org.hope.mcpservergenerate.model.http.HttpToolDefinition;
import org.hope.mcpservergenerate.scanner.SpringToolScanner;


import org.hope.mcpservergenerate.service.IService;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 关岁安
 * @since 2026/7/27
 */
@RequiredArgsConstructor
public class IServiceImpl implements IService {

   private final SpringToolScanner springToolScanner;
   private final ApplicationContext applicationContext;
   private final Converter<HttpToolDefinition> converter;
   private final HttpToolDefinitionContext httpToolDefinitionContext;

    @Override
    public void httpDataPipeline() {
        List<HttpToolDefinition> ans = new ArrayList<>();
        List<ToolDefinition> allHttpToolDefinitionList = springToolScanner.scan(applicationContext);
        for (ToolDefinition toolDefinition : allHttpToolDefinitionList) {
            List<HttpToolDefinition> convert = converter.convert(toolDefinition);
            if (convert != null && !convert.isEmpty()) {
                ans.addAll(convert);
            }
        }
        for (HttpToolDefinition an : ans) {
            System.out.println("拿到的工具定义为:"+an);
        }
        httpToolDefinitionContext.set(ans);
    }

}