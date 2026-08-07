package org.hope.mcpservergenerate.controller;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.service.impl.GenerateServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 关岁安
 * @since 2026/8/4
 */
@RestController
@RequestMapping("/generate")
@RequiredArgsConstructor
public class GenerateController {

    private GenerateServiceImpl generateService = new GenerateServiceImpl();

    @PostMapping("generate-framework")
    public String generateFramework() {
        generateService.generateFramework();
    }

}
