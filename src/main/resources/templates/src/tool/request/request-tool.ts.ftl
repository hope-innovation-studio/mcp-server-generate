import { McpTool } from "../../framework/decorator/tool-register.decorator";
import type { IMcpTool } from "../../framework/interface/tool.interface";
import * as z from "zod/v4";
import { HttpClient } from "../../framework/client/http-client";
import type { CallToolResult } from "@modelcontextprotocol/sdk/types.js";

@McpTool()
export class DifyRetrieveTool implements IMcpTool {
    name = "[=toolName?js_string]";
    description = "[=(description!'')?js_string]";

    inputSchema = z.object({
[#list allTsHttpParameter as parameter]
        [=parameter.key?js_string]: [=parameter.zodSchema][#if !parameter.required].optional()[/#if]
            .describe("[=(parameter.description!'')?js_string]"),
[/#list]
    });

    async handler(params: Record<string, unknown>): Promise<CallToolResult> {
        const input = this.inputSchema.parse(params);
        const httpInstance = HttpClient.getInstance();

        const result = await httpInstance.requestData<unknown>({
            method: "[=requestMethod?js_string]",
            url: "[=url?js_string]",
            params: {
[#if queryTsHttpParameter["UN_KNOW"]??]
[#list queryTsHttpParameter["UN_KNOW"] as parameter]
                [=parameter.key?js_string]: input["[=parameter.key?js_string]"],
[/#list]
[/#if]
[#if queryTsHttpParameter["PARAM"]??]
[#list queryTsHttpParameter["PARAM"] as parameter]
                [=parameter.key?js_string]: input["[=parameter.key?js_string]"],
[/#list]
[/#if]
            },
        });

        return {
            content: [
                {
                    type: "text",
                    text: typeof result === "string" ? result : JSON.stringify(result ?? null),
                },
            ],
        };
    }
}
