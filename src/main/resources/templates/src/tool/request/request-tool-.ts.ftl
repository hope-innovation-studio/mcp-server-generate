import {McpTool} from "../framework/decorator/tool-register.decorator";
import type {IMcpTool} from "../framework/interface/tool.interface";
import * as z from "zod/v4";
import {HttpClient} from "../framework/client/http-client";
import type {CallToolResult} from "@modelcontextprotocol/sdk/types.js";



@McpTool()
export class DifyRetrieveTool implements IMcpTool{
    name = [=(toolName)]
    description = "在具体的知识库进行检索"
    inputSchema = z.object({
        [#list parameters as parameter]
            "[=parameter.key]": [=parameter.type][#if !parameter.required].optional()[/#if],
        [/#list]
    });
    async handler(params: Record<string, unknown>): Promise<CallToolResult>{
        //解析参数
        const input  = this.inputSchema.parse(params);
        const httpInstance = HttpClient.getInstance();
        const result = await httpInstance.requestData<unknown>({
            method: [=requestMethod],
            url: [=url]
            params: {
                // QUERY / RequestParam
                [#list RequestParams as parameter]
                    [=parameter.key]: input.[=parameter.key],
                [/#list]
            },
            headers: {
                // HEADER / RequestHeader
                 [#list RequestHeaders as parameter]
                    [=parameter.key]: input.[=parameter.key],
                 [/#list]
            },
            data: {
                // BODY / RequestBody
                [#list RequestBodys as parameter]
                    [=parameter.key]: input.[=parameter.key],
                [/#list]
            },
        });

        return {
            content: [
                {
                    type: "text",
                    text: result,
                },
            ],
        };
    }
}
