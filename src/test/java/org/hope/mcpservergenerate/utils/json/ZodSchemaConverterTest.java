package org.hope.mcpservergenerate.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZodSchemaConverterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void convertsPrimitiveSchema() throws Exception {
        ObjectNode schema = (ObjectNode) objectMapper.readTree("{\"type\":\"integer\"}");

        assertEquals("z.number().int()", ZodSchemaConverter.toZodSchema(schema));
    }

    @Test
    void convertsArraySchema() throws Exception {
        ObjectNode schema = (ObjectNode) objectMapper.readTree("""
                {
                  "type": "array",
                  "items": { "type": "string" }
                }
                """);

        assertEquals("z.array(z.string())", ZodSchemaConverter.toZodSchema(schema));
    }

    @Test
    void convertsObjectSchemaAndMarksNonRequiredFieldsOptional() throws Exception {
        ObjectNode schema = (ObjectNode) objectMapper.readTree("""
                {
                  "type": "object",
                  "properties": {
                    "name": { "type": "string" },
                    "age": { "type": "integer" }
                  },
                  "required": ["name"]
                }
                """);

        assertEquals(
                "z.object({\"name\": z.string(), \"age\": z.number().int().optional()})",
                ZodSchemaConverter.toZodSchema(schema)
        );
    }

    @Test
    void convertsMapSchema() throws Exception {
        ObjectNode schema = (ObjectNode) objectMapper.readTree("""
                {
                  "type": "object",
                  "additionalProperties": { "type": "string" }
                }
                """);

        assertEquals(
                "z.record(z.string(), z.string())",
                ZodSchemaConverter.toZodSchema(schema)
        );
    }
}
