package org.hope.mcpservergenerate.utils.http;


import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 关岁安
 * @since 2026/7/22
 */
public class HttpMethodUtils {


    /**
     * 获取接口支持的所有请求方法集合以HttpMethod形式返回
     * @param method 方法定义信息
     * @return 返回这个接口支持的所有请求方法集合
     */
    public static List<HttpMethod> getHttpMethods(Method method){
        RequestMethod[] requestMethods = getRequestMethods(method);
        List<HttpMethod> ans = new ArrayList<>(requestMethods.length);
        for (RequestMethod requestMethod : requestMethods) {
            ans.add(HttpMethod.valueOf(requestMethod.name()));
        }
        return ans;
    }


    /**
     * 获取接口支持的所有请求方法集合以HttpMethod形式返回
     * @param method 方法定义信息
     * @return 返回这个接口支持的所有请求方法集合
     */
    private static RequestMethod[] getRequestMethods(Method method){
        RequestMapping mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(
                method,
                RequestMapping.class
        );
        //只有填了请求方式才能返回
        if (mergedAnnotation != null) {
            return mergedAnnotation.method();
        } else{
            /**
             * TODO 需要自定义异常，来告诉使用这个依赖程序员，有接口写错了
             */
            return null;
        }
    }

}
