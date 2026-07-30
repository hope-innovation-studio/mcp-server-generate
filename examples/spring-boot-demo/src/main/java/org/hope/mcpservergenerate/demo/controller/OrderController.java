package org.hope.mcpservergenerate.demo.controller;


import org.hope.mcpservergenerate.annotation.ExposeMcpTool;
import org.hope.mcpservergenerate.demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ExposeMcpTool(description = "根据订单号查询订单")
    @GetMapping("{orderId}")
    public Map<String, String> queryOrder(@PathVariable String orderId) {
        return orderService.queryOrder(orderId);
    }


    @ExposeMcpTool(name = "你好",description = "你好的描述")
    @GetMapping("ok")
    public String test(String s, @RequestParam String get){
        return "你好";
    }


    @ExposeMcpTool
    @RequestMapping(path = "put", method = {RequestMethod.POST, RequestMethod.PUT})
    public Data test1(){
        return new Data("关岁安",22);
    }

    @ExposeMcpTool
    @RequestMapping(path = "test2", method = {RequestMethod.POST, RequestMethod.PUT})
    public List<String> test2(){
        return null;
    }

    @ExposeMcpTool
    @RequestMapping(path = "test3", method = {RequestMethod.POST, RequestMethod.PUT})
    public Map<Data,Data> test3(){
        return null;
    }

}
