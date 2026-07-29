//package org.mcpservergenerate.scanner;
//
//import org.junit.jupiter.api.Test;
//import org.mcpservergenerate.annotation.ExposeMcpTool;
//import org.mcpservergenerate.model.ToolCandidate;
//import org.mcpservergenerate.model.ToolDefinition;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class ToolScannerTest {
//
//    @Test
//    void shouldCreateCandidateWithToolMetadataAndSourceMethod() {
//        List<ToolDefinition> candidates = new ToolScanner().scan(OrderController.class);
//
//        assertEquals(1, candidates.size());
//        assertEquals("queryOrder", candidates.get(0).getToolDefinition().getName());
//        assertEquals("根据订单号查询订单", candidates.get(0).getToolDefinition().getDescription());
//        assertEquals("queryOrder", candidates.get(0).getMethod().getName());
//    }
//
//    static class OrderController {
//
//        @ExposeMcpTool(description = "根据订单号查询订单")
//        public String queryOrder(String orderId) {
//            return orderId;
//        }
//
//        public String internalMethod() {
//            return "internal";
//        }
//    }
//}
