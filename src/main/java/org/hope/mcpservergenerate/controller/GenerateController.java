package org.hope.mcpservergenerate.controller;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.service.impl.GenerateServiceImpl;
import org.hope.mcpservergenerate.templateModel.ts.TsHttpToolTemplateModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author 关岁安
 * @since 2026/8/4
 */
@RestController
@RequestMapping("/generate")
@RequiredArgsConstructor
public class GenerateController {

    private final GenerateServiceImpl generateService;

    /**
     * 创建代码工作环境下
     * @param mcpName
     * @param mcpVersion
     * @param toolPath
     * @param projectName
     * @return
     */
    @PostMapping("framework-local")
    public R<String> generateFramework(String mcpName, String mcpVersion, String toolPath, String projectName) throws TemplateException, IOException {
        return generateService.generateFrameworkInLocal(mcpName, mcpVersion,toolPath,projectName);
    }

    /**
     * @param toolName 工具英文名字
     * @param toolId 工具名字
     * @return
     */
    @PostMapping("http-ts-tool")
    private R<String> generateToolInLocal(String toolName ,String toolId,String projectName) throws IOException {
        return generateService.generateToolInLocal(toolName,toolId,projectName);
    }

}
