package org.hope.mcpservergenerate.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.Map;

/**
 * 将 victools 生成的 JSON Schema 转换为可写入 TypeScript 模板的 Zod 表达式。
 */
public final class ZodSchemaConverter {

    private ZodSchemaConverter() {
    }

    public static String toZodSchema(ObjectNode schema) {
        String type = schema.path("type").asText();

        if ("string".equals(type)) {
            return "z.string()";
        }

        if ("integer".equals(type)) {
            return "z.number().int()";
        }

        if ("number".equals(type)) {
            return "z.number()";
        }

        if ("boolean".equals(type)) {
            return "z.boolean()";
        }

        if ("array".equals(type)) {
            JsonNode items = schema.get("items");
            if (items instanceof ObjectNode itemSchema) {
                return "z.array(" + toZodSchema(itemSchema) + ")";
            }
            return "z.array(z.unknown())";
        }

        if ("object".equals(type)) {
            return toZodObjectSchema(schema);
        }

        return "z.unknown()";
    }

    private static String toZodObjectSchema(ObjectNode schema) {
        JsonNode additionalProperties = schema.get("additionalProperties");
        if (additionalProperties instanceof ObjectNode valueSchema) {
            return "z.record(z.string(), " + toZodSchema(valueSchema) + ")";
        }

        JsonNode properties = schema.get("properties");
        if (!(properties instanceof ObjectNode objectProperties)) {
            return "z.record(z.string(), z.unknown())";
        }

        StringBuilder result = new StringBuilder("z.object({");
        Iterator<Map.Entry<String, JsonNode>> fields = objectProperties.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String propertyName = field.getKey();
            String propertySchema = toZodPropertySchema(field.getValue());

            result.append(quote(propertyName))
                    .append(": ")
                    .append(propertySchema);

            if (!isRequired(schema, propertyName)) {
                result.append(".optional()");
            }

            if (fields.hasNext()) {
                result.append(", ");
            }
        }

        return result.append("})").toString();
    }

    private static String toZodPropertySchema(JsonNode schema) {
        if (schema instanceof ObjectNode objectNode) {
            return toZodSchema(objectNode);
        }
        return "z.unknown()";
    }

    private static boolean isRequired(ObjectNode schema, String propertyName) {
        JsonNode requiredProperties = schema.get("required");
        if (requiredProperties == null || !requiredProperties.isArray()) {
            return false;
        }

        for (JsonNode requiredProperty : requiredProperties) {
            if (propertyName.equals(requiredProperty.asText())) {
                return true;
            }
        }

        return false;
    }

    private static String quote(String value) {
        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                + "\"";
    }
}
