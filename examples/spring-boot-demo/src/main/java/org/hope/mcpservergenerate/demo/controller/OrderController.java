package org.hope.mcpservergenerate.demo.controller;


import org.hope.mcpservergenerate.annotation.ExposeMcpTool;
import org.hope.mcpservergenerate.demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

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
}
