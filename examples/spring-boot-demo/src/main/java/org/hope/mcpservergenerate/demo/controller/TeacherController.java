package org.hope.mcpservergenerate.demo.controller;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.hope.mcpservergenerate.annotation.ExposeMcpTool;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 关岁安
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @ExposeMcpTool
    @GetMapping("get")
    public List<Data> get(@RequestParam String name) {
        System.out.println(name);
        return List.of(new Data("张三",10),
                new Data("李四",11),
                new Data("王五",12));
    }

    @ExposeMcpTool
    @GetMapping("get-teacher")
    public Data getTeacher(@RequestParam String name,
                           @RequestParam Integer age) {
        return new Data("关岁安",11);
    }

    @ExposeMcpTool
    @PostMapping("put-teacher")
    public Data putTeacher(@RequestParam String name,
                           @RequestParam Integer age) {
        return new Data("关岁安",11);
    }

    @ExposeMcpTool
    @PostMapping("delete-teacher")
    public Data delete(@RequestParam String name,
                           @RequestParam Integer age) {
        return new Data("关岁安",11);
    }


    @ExposeMcpTool
    @PostMapping("update-teacher")
    public Data update(@RequestParam String name,
                           @RequestParam Integer age) {
        return new Data("关岁安",11);
    }


    @ExposeMcpTool
    @PostMapping("insert-teacher")
    public Data insert(@RequestBody Data data) {
        return new Data("关岁安",11);
    }

}
