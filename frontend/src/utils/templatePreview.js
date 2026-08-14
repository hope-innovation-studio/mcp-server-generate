function pascalCase(value) {
  const normalized = String(value || 'Generated')
    .replace(/[^a-zA-Z0-9]+(.)/g, (_, character) => character.toUpperCase())
  return normalized.charAt(0).toUpperCase() + normalized.slice(1)
}

export function createTemplateVariables(node = {}) {
  const model = node.toolTemplateModel || {}
  return {
    toolName: model.toolName || 'generatedTool',
    description: model.description || '由 MCP Server Generate 创建的 Tool',
    requestMethod: model.requestMethod || 'GET',
    url: model.url || 'http://127.0.0.1:8080/api/example',
    parameters: model.allTsHttpParameter || [],
  }
}

export function renderStaticToolCode(variables) {
  const className = `${pascalCase(variables.toolName)}Tool`
  const description = String(variables.description || '').replaceAll('"', '\\"')
  return `import { McpTool } from "../framework/decorator/tool-register.decorator";
import type { IMcpTool } from "../framework/interface/tool.interface";
import { HttpClient } from "../framework/client/http-client";
import * as z from "zod/v4";

@McpTool()
export class ${className} implements IMcpTool {
    name = "${variables.toolName}";
    description = "${description}";
    inputSchema = z.object({});

    async handler(params: Record<string, unknown>) {
        const client = HttpClient.getInstance();
        const result = await client.requestData({
            method: "${variables.requestMethod}",
            url: "${variables.url}",
            params,
        });

        return {
            content: [{ type: "text", text: JSON.stringify(result) }],
        };
    }
}
`
}
