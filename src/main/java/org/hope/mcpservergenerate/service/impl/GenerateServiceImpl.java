package org.hope.mcpservergenerate.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.http.HttpParameterDefinition;
import org.hope.mcpservergenerate.model.http.HttpToolDefinition;
import org.hope.mcpservergenerate.model.http.enums.HttpParameterLocation;
import org.hope.mcpservergenerate.templateModel.ts.TsHttpToolParameterTemplateModel;
import org.hope.mcpservergenerate.templateModel.ts.TsHttpToolTemplateModel;
import org.hope.mcpservergenerate.utils.json.ZodSchemaConverter;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 关岁安
 */
@Component
@RequiredArgsConstructor
public class GenerateServiceImpl {

    private final Configuration freemarkerConfig;

    private final HttpToolDefinitionContext httpToolDefinitionContext;

    private final String ipHost = "http://127.0.0.1:8080/";

    private final String OUTPUT_PATH = "OUTPUT/";

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

    /**
     *
     * TODO 还需要加很多的东西
     * @param toolName
     * @param toolId
     * @return
     */
    public R<String> generateToolInLocal(String toolName,String toolId,String projectName) throws IOException {
        TsHttpToolTemplateModel tsHttpToolTemplateModel = new TsHttpToolTemplateModel();
        HttpToolDefinition httpToolDefinition = httpToolDefinitionContext.get().get(toolId);
        String url = ipHost + httpToolDefinition.getEndpoint();
        //获取转化后的参数
        List<TsHttpToolParameterTemplateModel> tsHttpToolParameterTemplateModels = constructionTsHttpParam(httpToolDefinition.getParameters());
        //按照local分一下组
        Map<String, List<TsHttpToolParameterTemplateModel>> collect = tsHttpToolParameterTemplateModels.stream().collect(Collectors.groupingBy(TsHttpToolParameterTemplateModel::getLocation));
        tsHttpToolTemplateModel.setToolName(toolName)
                .setUrl(url)
                .setDescription(httpToolDefinition.getDescription())
                .setRequestMethod(httpToolDefinition.getRequestMethod())
                .setAllTsHttpParameter(tsHttpToolParameterTemplateModels)
                .setQueryTsHttpParameter(collect);
        Template template = freemarkerConfig.getTemplate(
                "src/tool/request/request-tool.ts.ftl"
        );
        //目标路径： OUT/projectName/tool/toolName.ts
        //路径：OUT/
        Path outputFile = Path.of(OUTPUT_PATH + projectName + "/" + toolName + ".ts");
        Files.createDirectories(outputFile.getParent());
        try (Writer writer = Files.newBufferedWriter(
                outputFile,
                StandardCharsets.UTF_8
        )) {
            template.process(tsHttpToolTemplateModel, writer);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        return R.success("成功添加");


    }

    List<TsHttpToolParameterTemplateModel> constructionTsHttpParam(List<HttpParameterDefinition> list){
        List<TsHttpToolParameterTemplateModel> ans = new ArrayList<>();
        for (HttpParameterDefinition httpParameterDefinition : list) {
            TsHttpToolParameterTemplateModel tsHttpToolParameterTemplateModel = new TsHttpToolParameterTemplateModel();
            String zodSchema = ZodSchemaConverter.toZodSchema(httpParameterDefinition.getType());
            tsHttpToolParameterTemplateModel
                    .setKey(httpParameterDefinition.getKey())
                    .setZodSchema(zodSchema)
                    .setLocation(httpParameterDefinition.getLocation().name());
            ans.add(tsHttpToolParameterTemplateModel);
        }
        return ans;
    }


}
