package org.hope.mcpservergenerate.annotation;

import java.lang.annotation.*;

/**
 * 暴露工具的注解
 * @author 关岁安
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeMcpTool {
    String name() default "";
    String description();
}
