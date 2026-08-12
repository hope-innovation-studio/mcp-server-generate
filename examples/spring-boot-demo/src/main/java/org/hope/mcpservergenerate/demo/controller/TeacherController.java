package org.hope.mcpservergenerate.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 关岁安
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping("get")
    public List<Data> get(@RequestParam String name) {
        System.out.println(name);
        return List.of(new Data("张三",10),
                new Data("李四",11),
                new Data("王五",12));
    }

}
