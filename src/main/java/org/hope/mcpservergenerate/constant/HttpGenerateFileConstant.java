package org.hope.mcpservergenerate.constant;

import java.util.List;

/**
 * @author 关岁安
 * @since 2026/8/12
 */
public final class HttpGenerateFileConstant {

    /**
     * 创建请求的模板路径
     */
    public static final String REQUEST_FILE_PATH = "src/tool/request/request-tool.ts.ftl";
    /**
     * 生成framework的模板路径
     */
    public static final List<String> FRAMEWORKS_FILE_PATH =List.of(
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


}
