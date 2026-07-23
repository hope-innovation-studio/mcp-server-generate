package org.mcpservergenerate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mcpservergenerate.model.enums.HttpParameterLocation;

/**
 * 参数
 * @author 关岁安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpParameterDefinition {

    /**
     * 参数名字
     */
    private String key;

    /**
     * 参数所在位置。
     */
    private HttpParameterLocation location;

    /**
     * 是否必填。
     */
    private Boolean required;


}
