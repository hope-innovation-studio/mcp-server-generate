package org.hope.mcpservergenerate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * 工具定义
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolDefinition {

    /**
     * 工具名字
     * 首先从注解去
     * 然后
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    @JsonIgnore
    private Method method;
}
