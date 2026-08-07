import type { ZodObject, ZodRawShape } from "zod/v4";

/**
 * 定义Tool的规范
 */
export interface IMcpTool {
    name: string;
    description: string;
    inputSchema: ZodObject<ZodRawShape>;
    handler(params: Record<string, any>): Promise<any>;
}