package org.hope.mcpservergenerate.generate;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


public class GenerateTest {


    /**
     * 生成frame-work-index文件。
     * 用户可以指定文件地址
     */
    @Test
    public void generateIndex() throws IOException {
        String mcpName = "mcp-generate-index";
        Path targetProjectDir = Path.of("D:\\javacunfangchu\\mcp-server-generate\\src\\main\\resources");

        Configuration freemarkerConfig = createFreemarkerConfiguration();

        Template template = freemarkerConfig.getTemplate(
                "src/framework/start.application.ts.ftl"
        );

        Map<String, Object> dataModel = Map.of(
                "mcpName", mcpName,
                "version", "1.0.1"
        );

        Path outputFile = targetProjectDir
                .resolve("src/framework/start.application.ts");

        Files.createDirectories(outputFile.getParent());

        try (Writer writer = Files.newBufferedWriter(
                outputFile,
                StandardCharsets.UTF_8
        )) {
            template.process(dataModel, writer);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void generateHttpClient() throws IOException{
        String mcpName = "mcp-generate-http-client";
        Path targetProjectDir = Path.of("D:\\javacunfangchu\\mcp-server-generate\\src\\main\\resources");

        Configuration freemarkerConfig = createFreemarkerConfiguration();

        Template template = freemarkerConfig.getTemplate(
                "src/framework/client/http-client.ts.ftl"
        );

        Map<String, Object> dataModel = Map.of(
                "mcpName", mcpName,
                "mcpVersion", "1.0.0"
        );

        Path outputFile = targetProjectDir
                .resolve("src/framework/http-client.ts");

        Files.createDirectories(outputFile.getParent());

        try (Writer writer = Files.newBufferedWriter(
                outputFile,
                StandardCharsets.UTF_8
        )) {
            template.process(dataModel, writer);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }


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

}
