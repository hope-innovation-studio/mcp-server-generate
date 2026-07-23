package org.mcpservergenerate.utils.http;

import org.mcpservergenerate.model.HttpParameterDefinition;
import org.mcpservergenerate.model.enums.HttpParameterLocation;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mcpservergenerate.model.enums.HttpParameterLocation.*;

/**
 * @author 关岁安
 * @since 2026/7/22
 */
public class HttpParamUtils {

    /**
     * 这里可以使用二进制的方式
     * 为什么使用二进制：
     * 1. 可以表达唯一性 n & (n - 1)
     * 2. 在唯一性确定的条件下，n可以确定这个参数使用了哪个注解
     * 3。 numberOfTrailingZeros方法可以算出后置零
     * 比如256在二进制中表示为0b1000000000，这个方法可以得到有1后面有8个零
     * @param method 方法定义信息
     */
    public static List<HttpParameterDefinition> getHttpParam(Method method){
        Parameter[] parameters = method.getParameters();
        List<HttpParameterDefinition> ans = new ArrayList<>();
        for (Parameter parameter : parameters) {
            int number = 0;
            for (int i = 1; i <= 8; i++) {
                Class<? extends Annotation> location = fromOrder(i);
                if (location != null && parameter.isAnnotationPresent(location)) {
                    number |= (1 << (9 - i));
                }
            }
            if (number == 0) {
                number |= 1;
            }
            if ((number & (number - 1)) == 0) {
                int trailingZeros = Integer.numberOfTrailingZeros(number);
                int position = trailingZeros + 1;
                String key = parameter.getName();
                HttpParameterLocation location = getHttpParameterLocation(position);
                Boolean required = isRequired(parameter, location);
                ans.add(new HttpParameterDefinition(key,location,required));
            } else {
                /**
                 * TODO 设置自定的异常
                 */
                throw new IllegalArgumentException("参数 [" + parameter.getName() + "] 包含了多个互斥的 HTTP 位置注解！");
            }
        }
        /**
         * TODO 需要判断这个ans的长度
         */
        return ans;
    }

    /**
     * 根据参数和提取到的 location，解析 required
     */
    private static boolean isRequired(Parameter parameter, HttpParameterLocation location) {
        if (location == null || location == HttpParameterLocation.UN_KNOW) {
            // 没有打注解的普通参数，默认非必填（或者根据是否是基本数据类型判断）
            // 如果是 int 等基本类型必须有值，这里可根据你的业务调整
            return !parameter.getType().isPrimitive();
        }

        switch (location) {
            case PARAM:
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                // 注意：如果配置了 defaultValue，即使 required 为 true，Spring 也会填充默认值，因此可认为非必填
                return requestParam.required() && ValueConstants.DEFAULT_NONE.equals(requestParam.defaultValue());

            case PATH:
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                return pathVariable.required();

            case HEADER:
                RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
                return requestHeader.required() && ValueConstants.DEFAULT_NONE.equals(requestHeader.defaultValue());

            case COOKIE:
                CookieValue cookieValue = parameter.getAnnotation(CookieValue.class);
                return cookieValue.required() && ValueConstants.DEFAULT_NONE.equals(cookieValue.defaultValue());

            case BODY:
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                return requestBody.required();

            case MULTIPART:
                RequestPart requestPart = parameter.getAnnotation(RequestPart.class);
                return requestPart.required();

            case MATRIX:
                MatrixVariable matrixVariable = parameter.getAnnotation(MatrixVariable.class);
                return matrixVariable.required();

            case MODEL_ATTRIBUTE:
            default:
                return false;
        }
    }


}
