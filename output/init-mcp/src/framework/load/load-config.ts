import { readFile } from "node:fs/promises";
import * as path from "node:path";
import type { Config } from "../interface/system.interface";

export async function loadConfig(): Promise<Config> {
    const configPaths = [
        process.env.CONFIG_PATH,
        path.resolve(process.cwd(), "config", "config.json"),
        path.resolve(process.cwd(), "config.json"),
    ]
        .filter((configPath): configPath is string => Boolean(configPath))
        .map(configPath => path.resolve(configPath));

    for (const configPath of configPaths) {
        try {
            const jsonContent = await readFile(configPath, "utf-8");
            return JSON.parse(jsonContent) as Config;
        } catch (error) {
            const nodeError = error as NodeJS.ErrnoException;

            // 当前文件不存在，继续寻找下一个路径
            if (nodeError.code === "ENOENT") {
                continue;
            }

            // JSON 格式错误、权限错误等问题直接抛出
            throw new Error(
                `配置文件加载失败：${configPath}\n${nodeError.message}`,
                { cause: error },
            );
        }
    }

    throw new Error(
        [
            "找不到配置文件，已经查找以下路径：",
            ...configPaths.map(configPath => `- ${configPath}`),
            "也可以通过 CONFIG_PATH 环境变量指定配置文件。",
        ].join("\n"),
    );
}