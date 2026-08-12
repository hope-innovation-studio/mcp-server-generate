package org.hope.mcpservergenerate.utils.id;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.lang.Snowflake;

/**
 * @author 关岁安
 * 生成雪花算法
 */
public final class SnowflakeIdGenerator {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    private SnowflakeIdGenerator() {
    }

    public static String nextId() {
        return SNOWFLAKE.nextIdStr();
    }
}