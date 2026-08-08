import { McpTool } from "../../framework/decorator/tool-register.decorator";
import type { IMcpTool } from "../../framework/interface/tool.interface";
import * as z from "zod/v4";
import { HttpClient } from "../../framework/client/http-client";
import type { CallToolResult } from "@modelcontextprotocol/sdk/types.js";

@McpTool()
export class DifyRetrieveTool implements IMcpTool {
    name = "select-tool";
    description = "根据订单号查询订单";

    inputSchema = z.object({
        orderId: z.string().optional()
            .describe(""),
    });

    async handler(params: Record<string, unknown>): Promise<CallToolResult> {
        const input = this.inputSchema.parse(params);
        const httpInstance = HttpClient.getInstance();

        const result = await httpInstance.requestData<unknown>({
            method: "GET",
            url: "http://127.0.0.1:8080//orders/{orderId}",
            params: {
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
