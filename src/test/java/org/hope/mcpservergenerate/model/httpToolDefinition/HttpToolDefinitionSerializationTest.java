package org.hope.mcpservergenerate.model.httpToolDefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpToolDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpToolDefinitionSerializationTest {

    @Test
    void serializesReturnSchemaAsJsonSchema() throws Exception {
        SchemaGeneratorConfig config = new SchemaGeneratorConfigBuilder(
                SchemaVersion.DRAFT_2020_12,
                OptionPreset.PLAIN_JSON
        ).build();
        ObjectNode schema = new SchemaGenerator(config).generateSchema(String.class);
        HttpToolDefinition definition = new HttpToolDefinition().setReturnType(schema);

        JsonNode response = new ObjectMapper().readTree(
                new ObjectMapper().writeValueAsString(definition)
        );

        assertEquals("string", response.path("returnType").path("type").asText());
    }
}
