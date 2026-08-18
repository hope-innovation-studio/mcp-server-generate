package org.hope.mcpservergenerate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenerateControllerTest {

    @Test
    void previewEndpointShouldAcceptPostRequestsWithJsonBody() throws NoSuchMethodException {
        Method method = GenerateController.class.getDeclaredMethod(
                "previewTsHttpCode",
                org.hope.mcpservergenerate.model.dto.preview.PreviewHttpTsTemplate.class
        );

        assertNotNull(method.getAnnotation(PostMapping.class));
    }
}
