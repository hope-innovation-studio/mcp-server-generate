package org.hope.mcpservergenerate.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.model.R;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.Map;

/**
 * @author 关岁安
 */
@Component
@RequiredArgsConstructor
public class GenerateServiceImpl {

    private final Configuration freemarkerConfig;

    private final List<String> FRAMEWORKS_PATH =List.of(
            "package.json",
            "tsconfig.json",
            "src/index.ts.ftl",
            "src/framework/start.application.ts.ftl",
            "src/framework/client/http-client.ts",
            "src/framework/decorator/tool-register.decorator.ts",
            "src/framework/interface/response.interface.ts",
            "src/framework/interface/system.interface.ts",
            "src/framework/interface/tool.interface.ts",
            "src/framework/load/load-config.ts",
            "src/framework/load/load-tool.ts"
    );

    public R<String> generateFrameworkInLocal (String mcpName, String mcpVersion, String toolPath, String projectName) throws IOException, TemplateException {
        toolPath = toolPath == null ? "" : toolPath;
        Map<String ,Object> dataModel = Map.of(
                "mcpName", mcpName,
                "mcpVersion", mcpVersion,
                "toolPath", toolPath
        );
        Path outputPath = Path.of("output/" + projectName);
        for (String fileName : FRAMEWORKS_PATH) {
            String outputName = fileName.endsWith(".ftl")
                    ? fileName.substring(0, fileName.length() - 4)
                    : fileName;

            //先拼接 在取父亲
            Path outputFile = outputPath.resolve(outputName);
            Files.createDirectories(outputFile.getParent());

            if (fileName.endsWith(".ftl")) {
                Template template = freemarkerConfig.getTemplate(fileName);

                try (Writer writer = Files.newBufferedWriter(
                        outputFile,
                        StandardCharsets.UTF_8
                )) {
                    template.process(dataModel, writer);
                }

            } else {
                try (InputStream inputStream = getClass()
                        .getClassLoader()
                        .getResourceAsStream("templates/" + fileName)) {

                    if (inputStream == null) {
                        throw new IOException("找不到模板文件：" + fileName);
                    }

                    Files.copy(
                            inputStream,
                            outputFile,
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }
            }
        }
        return R.success("创建成功");
    }




}
