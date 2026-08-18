package org.hope.mcpservergenerate.converter.impl;

import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolParameterTemplateModel;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpParameterDefinition;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpToolDefinition;
import org.hope.mcpservergenerate.utils.json.ZodSchemaConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 关岁安
 * @since 2026/8/14
 * 将工具定义转化为模板实体
 */
@Component
public class TsHttpToolTemplateModelConverter {

    /**
     * 将HttpToolDefinition转化模型定义
     * @param className
     * @param toolName
     * @param definition
     * @param baseUrl
     * @return
     */
    public TsHttpToolTemplateModel convert(
            String className,
            String toolName,
            HttpToolDefinition definition,
            String baseUrl
    ) {
        List<TsHttpToolParameterTemplateModel> parameters =
                convertParameters(definition.getParameters());

        Map<String, List<TsHttpToolParameterTemplateModel>> groupedParameters =
                parameters.stream()
                        .collect(Collectors.groupingBy(
                                TsHttpToolParameterTemplateModel::getLocation
                        ));

        return new TsHttpToolTemplateModel()
                .setClassName(className)
                .setToolName(toolName)
                .setUrl(baseUrl + definition.getEndpoint())
                .setDescription(definition.getDescription())
                .setRequestMethod(definition.getRequestMethod())
                .setAllTsHttpParameter(parameters)
                .setQueryTsHttpParameter(groupedParameters);
    }

    private List<TsHttpToolParameterTemplateModel> convertParameters(
            List<HttpParameterDefinition> parameters
    ) {
        List<TsHttpToolParameterTemplateModel> result = new ArrayList<>();

        for (HttpParameterDefinition parameter : parameters) {
            result.add(
                    new TsHttpToolParameterTemplateModel()
                            .setKey(parameter.getKey())
                            .setLocation(parameter.getLocation().name())
                            .setZodSchema(
                                    ZodSchemaConverter.toZodSchema(parameter.getType())
                            )
            );
        }

        return result;
    }
}
