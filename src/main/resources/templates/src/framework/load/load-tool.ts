import * as fs from "node:fs/promises";
import * as path from "node:path";
import {getToolList} from "../decorator/tool-register.decorator";
import {pathToFileURL} from "node:url";
/**
 * 递归扫描指定目录下的所有 .ts 文件
 * @param dirPath 目标目录路径
 * @returns 所有 .ts 文件的绝对路径列表
 */
export async function scanTsFiles(dirPath: string): Promise<string[]> {
    const tsFiles: string[] = [];
    const entries = await fs.readdir(dirPath, { withFileTypes: true });

    for (const entry of entries) {
        const fullPath = path.join(dirPath, entry.name);

        if (entry.isDirectory()) {
            tsFiles.push(...await scanTsFiles(fullPath));
        } else if (
            entry.isFile() &&
            [".ts", ".js"].includes(path.extname(entry.name)) &&
            !entry.name.endsWith(".d.ts")
        ) {
            tsFiles.push(fullPath);
        }
    }

    return tsFiles;
}


/**
 * TODO 这个扫描路径总是出问题
 * 1. 无法确定项目路径
 * 2. 会将一些无关的包扫描进来
 * 确定只扫描项目目录下src下的所有ts文件
 * @param toolsDir
 */
export async function loadTools(toolsDir: string = ""): Promise<void> {
    const projectRoot = path.resolve(__dirname, "../../..");
    const sourceDir = path.resolve(projectRoot, toolsDir);
    const filePaths = await scanTsFiles(sourceDir);
    for (const filePath of filePaths) {
        await import(pathToFileURL(path.resolve(filePath)).href);
    }
}

