package org.hope.mcpservergenerate.demo.controller;

import org.hope.mcpservergenerate.annotation.ExposeMcpTool;
import org.springframework.web.bind.annotation.*;

/**
 * @author 关岁安
 */
@RestController("order")
public class OrderController {

    @ExposeMcpTool(name = "你好", description = "哈哈")
    @GetMapping("get")
    public String get(String s, @RequestParam String param){
        return s;
    }

    @PostMapping("post")
    public String post(@RequestParam String string){
        return "你好i";
    }

    @ExposeMcpTool
    @RequestMapping(path = "guansuian", method = {RequestMethod.GET, RequestMethod.POST})
    public String put(){
        return "你好";
    }

}
