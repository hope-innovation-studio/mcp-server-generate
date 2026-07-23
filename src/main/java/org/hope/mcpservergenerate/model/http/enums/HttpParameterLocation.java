package org.hope.mcpservergenerate.model.http.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对http请求参数的枚举
 * @author 关岁安
 * @since 2026/7/22
 * 一共9种情况
 * n & (n - 1):使最右边的为1的位置变为0
 *
 */
@Getter
@RequiredArgsConstructor
public enum HttpParameterLocation {
    /**
     * PathVariable 注解下的修饰的变量
     * 从左->右边第一个
     */
    PATH(PathVariable.class, 1),
    /**
     * RequestParam
     */
    PARAM(RequestParam.class,2),
    /**
     * MatrixVariable
     */
    MATRIX(MatrixVariable.class,3),
    /**
     * RequestHeader
     */
    HEADER(RequestHeader.class,4),
    /**
     * CookieValue
     */
    COOKIE(CookieValue.class,5),
    /**
     * RequestBody
     */
    BODY(RequestBody.class ,6),
    /**
     *RequestPart
     */
    MULTIPART(RequestPart.class , 7),
    /**
     * ModelAttribute
     */
    MODEL_ATTRIBUTE(ModelAttribute.class , 8),
    /**
     * 这个参数前面没有打任何注解
     */
    UN_KNOW(null,9);

    private final Class<? extends Annotation> annotationType;

    private final int order;

    /**
     * 构造
     * key:order
     * value:HttpParameterLocation
     * 类型的map
     */
    private static final Map<Integer, HttpParameterLocation> LOCATION_BY_ORDER =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            HttpParameterLocation::getOrder,
                            Function.identity()
                    ));

    public static Class<? extends Annotation> fromOrder(int order) {
        HttpParameterLocation location = LOCATION_BY_ORDER.get(order);
        if (location == null) {
            throw new IllegalArgumentException("不支持的 order: " + order);
        }
        return location.getAnnotationType();
    }

    public static HttpParameterLocation getHttpParameterLocation(Integer position) {
        return LOCATION_BY_ORDER.get(position);
    }
}