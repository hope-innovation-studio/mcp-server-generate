import { loadTools } from "./load/load-tool"
import { getToolList } from "./decorator/tool-register.decorator"
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js"
import { loadConfig } from "./load/load-config"
import { HttpClient } from "./client/http-client"
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js"

/**
 * Generated MCP Server entry point.
 */
export async function run(mcpName?: string, toolDir?: string) {
    const server = new McpServer({
        name: mcpName ,
        version: "1.0.0",
    })

    const config = await loadConfig()
    HttpClient.getInstance(config)

    if (toolDir) {
        await loadTools(toolDir)
    }

    for (const ToolDefinition of getToolList()) {
        const tool = new ToolDefinition()
        server.registerTool(
            tool.name,
            {
                description: tool.description,
                inputSchema: tool.inputSchema,
            },
            async (params: Record<string, unknown>) => tool.handler(params)
        )
    }

    await server.connect(new StdioServerTransport())
}
