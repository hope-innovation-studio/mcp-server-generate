package org.hope.mcpservergenerate.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author 关岁安
 * @since 2026/8/18
 * TODO 之后这个业务层所有的类的起名需要再讲究一下
 */
@RequiredArgsConstructor
public class FreeMarkerService {

    StringWriter writer = new StringWriter();

    private final Configuration freemarkerConfig;

    public String process(String templatePath, TsHttpToolTemplateModel tsHttpToolTemplateModel) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(
                templatePath
        );
        StringWriter writer = new StringWriter();

        template.process(
                tsHttpToolTemplateModel,
                writer
        );
        return writer.toString();
    }

}
