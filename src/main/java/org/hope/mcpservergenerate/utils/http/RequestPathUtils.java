package org.hope.mcpservergenerate.utils.http;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 关岁安
 * @since 2026/7/22
 * 请求参数获取器
 */
public class RequestPathUtils {


    /**
     * 获取请求路径
     * @param method 方法定义信息
     * @return 请求路径
     */
    public static List<String> getRequestPath(Method method){
        RequestMapping methodRequestMapping = getMethodRequesMapping(method);
        RequestMapping classRequestMapping = getClassRequesMapping(method);
        String[] methodPaths = getPaths(methodRequestMapping);
        String[] classPaths = getPaths(classRequestMapping);
        List<String> ans = new ArrayList<>();
        for (String classPath : classPaths) {
            for (String methodPath : methodPaths) {
                ans.add(classPath + "/" + methodPath);
            }
        }
        return ans;
    }


    /**
     * 方法级别下的请求注解信息
     * @param method 方法定义信息
     * @return RequestMapping注解的值
     */
    private static RequestMapping getMethodRequesMapping(Method method){
        // 方法上的 @RequestMapping、@GetMapping、@PostMapping 等
        return AnnotatedElementUtils.findMergedAnnotation(
                method,
                RequestMapping.class
        );
    }

    /**
     * 类级别下的请求注解信息
     * @param method 方法定义信息
     * @return RequestMapping注解的值
     */
    private static RequestMapping getClassRequesMapping(Method method){
        // 获取 Controller 类
        Class<?> controllerClass = method.getDeclaringClass();
        return AnnotatedElementUtils.findMergedAnnotation(
                controllerClass,
                RequestMapping.class
        );
    }

    /**
     * 通过RequestMapping 获取到请求路径
     * @param mapping 请求注解
     * @return 路径
     */
    private static String[] getPaths(RequestMapping mapping) {
        if (mapping == null) {
            return null;
        }
        // Spring 新版本优先使用 path
        if (mapping.path().length > 0) {
            return mapping.path();
        }
        // 兼容 value 写法
        if (mapping.value().length > 0) {
            return mapping.value();
        }
        return null;
    }
}
