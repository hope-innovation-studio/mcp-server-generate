package org.hope.mcpservergenerate.demo.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {

    public Map<String, String> queryOrder(String orderId) {
        return Map.of(
                "orderId", orderId,
                "status", "CREATED"
        );
    }
}
