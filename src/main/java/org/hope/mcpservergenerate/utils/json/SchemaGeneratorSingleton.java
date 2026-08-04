package org.hope.mcpservergenerate.utils.json;

import com.github.victools.jsonschema.generator.*;

/**
 * @author 关岁安
 * @since 2026/7/30
 * 单例模式
 * 用来将Type信息转化为可以传输的结构
 */
public class SchemaGeneratorSingleton {

    private static  SchemaGenerator instance;


    private SchemaGeneratorSingleton() {
    }

    public static SchemaGenerator getInstance() {
        if (instance == null) {
            SchemaGeneratorConfig config = new SchemaGeneratorConfigBuilder(
                    SchemaVersion.DRAFT_2020_12,
                    OptionPreset.PLAIN_JSON
            )
                    .without(Option.SCHEMA_VERSION_INDICATOR)
                    .with(Option.MAP_VALUES_AS_ADDITIONAL_PROPERTIES)
                    .build();
            instance = new SchemaGenerator(config);
        }
        return instance;
    }

}
