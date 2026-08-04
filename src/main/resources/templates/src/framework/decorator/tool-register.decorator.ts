// src/tool/tool-registry.ts
import type { IMcpTool } from "../interface/tool.interface";
// 定义一个名为 ToolConstructor 的类型，用来约束变量必须是一个“可以 new 出 IMcpTool 实例”的类（Class）
type ToolConstructor = new () => IMcpTool;
//就是说type一般是用来让集合装东西，应为最后会设工程
const toolList = new Array<ToolConstructor>


/**
 * 定义装饰器
 * {
 *     装饰器
 *     业务代码
 *     装饰器
 * }
 * @constructor
 */
export function McpTool(require:boolean = true){
    return function <T extends ToolConstructor>(
        target: T,
        _context: ClassDecoratorContext<T>,
    ): void {
        if(require){
            toolList.push(target)
        }
    };
}

/**
 * 获取到所有tool
 */
export function getToolList(): ToolConstructor[]{
    return [...toolList];
}

