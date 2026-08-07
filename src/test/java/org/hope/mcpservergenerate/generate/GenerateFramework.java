package org.hope.mcpservergenerate.generate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GenerateFramework {

    public Configuration createFreemarkerConfiguration() throws IOException {
        Configuration configuration = new Configuration(
                Configuration.VERSION_2_3_34
        );

        configuration.setClassLoaderForTemplateLoading(
                getClass().getClassLoader(),
                "/templates"
        );

        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(
                TemplateExceptionHandler.RETHROW_HANDLER
        );
        configuration.setInterpolationSyntax(
                Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX
        );

        return configuration;
    }


    @Test
    public void generate() throws IOException, TemplateException {
        String projectName = "init-mcp";
        List<String> files = List.of(
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
        Map<String, Object> dataModel = Map.of(
                "mcpName", "order-mcp-server",
                "mcpVersion", "1.0.4",
                "toolPath", ""
        );
        Path outputPath = Path.of("output/" + projectName);
        Configuration configuration = createFreemarkerConfiguration();


        for (String fileName : files) {
            String outputName = fileName.endsWith(".ftl")
                    ? fileName.substring(0, fileName.length() - 4)
                    : fileName;

            //先拼接 在取父亲
            Path outputFile = outputPath.resolve(outputName);
            Files.createDirectories(outputFile.getParent());

            if (fileName.endsWith(".ftl")) {
                Template template = configuration.getTemplate(fileName);

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
    }




}
