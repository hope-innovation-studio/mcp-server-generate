package org.hope.mcpservergenerate.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpParameterDefinition;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpToolDefinition;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolParameterTemplateModel;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;

import org.hope.mcpservergenerate.model.tree.FileTreePrinter;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.hope.mcpservergenerate.model.tree.httptemplatenode.TsHttpStaticTemplateFileNode;
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

import static org.hope.mcpservergenerate.constant.HttpGenerateFileConstant.FRAMEWORKS_FILE_PATH;


/**
 * @author 关岁安
 */
@Component
@RequiredArgsConstructor
public class GenerateServiceImpl {

    private final Configuration freemarkerConfig;

    private final HttpToolDefinitionContext httpToolDefinitionContext;

    private final HttpFileTreeContext httpFileTreeContext;

    private final String ipHost = "http://127.0.0.1:8080/";

    private final String OUTPUT_PATH = "OUTPUT/";



    public R<String> generateFrameworkInLocal (String mcpName, String mcpVersion, String toolPath, String projectName) throws IOException, TemplateException {
        toolPath = toolPath == null ? "" : toolPath;
        Map<String ,Object> dataModel = Map.of(
                "mcpName", mcpName,
                "mcpVersion", mcpVersion,
                "toolPath", toolPath
        );
        Path outputPath = Path.of("output/" + projectName);
        for (String fileName : FRAMEWORKS_FILE_PATH) {
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


    /**
     * 初始化根目录
     * @param path 初始化路径
     * @return 是否成功
     */
    public R<String> initHttpTsRootFolder(String path){
        this.httpFileTreeContext.getRoot().setPath(path);
        return R.success("成功初始化mcp根目录");
    }

    public R<FolderNode> initFrameworkTsFolder() {
        FolderNode root = (FolderNode) httpFileTreeContext.getRoot();
        FolderNode src = new FolderNode("src");
        root.add(src);
        FolderNode framework = new FolderNode("src/framework");
        src.add(framework);
        FolderNode client = new FolderNode("src/framework/client");
        framework.add(client);
        FolderNode decorator = new FolderNode("src/framework/decorator");
        framework.add(decorator);
        FolderNode interfaces = new FolderNode("src/framework/interface");
        framework.add(interfaces);
        FolderNode load = new FolderNode("src/framework/load");
        framework.add(load);

        client.add(new TsHttpStaticTemplateFileNode(
                "src/framework/client/http-client.ts"
        ));

        decorator.add(new TsHttpStaticTemplateFileNode(
                "src/framework/decorator/tool-register.decorator.ts"
        ));

        interfaces.add(new TsHttpStaticTemplateFileNode(
                "src/framework/interface/response.interface.ts"
        ));

        interfaces.add(new TsHttpStaticTemplateFileNode(
                "src/framework/interface/system.interface.ts"
        ));

        interfaces.add(new TsHttpStaticTemplateFileNode(
                "src/framework/interface/tool.interface.ts"
        ));

        load.add(new TsHttpStaticTemplateFileNode(
                "src/framework/load/load-config.ts"
        ));

        load.add(new TsHttpStaticTemplateFileNode(
                "src/framework/load/load-tool.ts"
        ));
        String print = FileTreePrinter.print(root);
        System.out.println(print);
        return R.success(root);
    }
}
