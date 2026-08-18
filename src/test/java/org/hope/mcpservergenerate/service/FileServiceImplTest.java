package org.hope.mcpservergenerate.service;

import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.converter.impl.TsHttpToolTemplateModelConverter;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.templatemodel.ts.TsHttpToolTemplateModel;
import org.hope.mcpservergenerate.model.tooldefinition.httptooldefinition.HttpToolDefinition;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.hope.mcpservergenerate.model.tree.httptemplatenode.TsHttpToolTemplateFileNode;
import org.hope.mcpservergenerate.service.impl.FileServiceImpl;
import org.hope.mcpservergenerate.service.impl.TreeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hope.mcpservergenerate.constant.HttpGenerateFileConstant.REQUEST_FILE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileServiceImplTest {

    private FolderNode root;
    private FolderNode toolsFolder;
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        root = new FolderNode("default");
        toolsFolder = new FolderNode("default/src/tool");
        root.add(toolsFolder);

        HttpFileTreeContext treeContext = new HttpFileTreeContext(root);
        HttpToolDefinitionContext toolContext = new HttpToolDefinitionContext();
        HttpToolDefinition definition = new HttpToolDefinition();
        definition.setId("tool-1");
        definition.setEndpoint("/orders/get");
        definition.setRequestMethod("GET");
        definition.setDescription("查询订单");
        definition.setParameters(new ArrayList<>());
        toolContext.get().put(definition.getId(), definition);

        fileService = new FileServiceImpl(
                treeContext,
                new TreeServiceImpl(treeContext),
                new TsHttpToolTemplateModelConverter(),
                toolContext
        );
    }

    @Test
    void shouldAddHttpToolTemplateFileToFolder() {
        R<TsHttpToolTemplateFileNode<TsHttpToolTemplateModel>> result =
                fileService.addHttpTsToolToFolder("QueryOrderTool", "queryOrder", toolsFolder.getId(), "tool-1");

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("default/src/tool/queryOrder.ts", result.getData().getPath());
        assertEquals(REQUEST_FILE_PATH, result.getData().getTemplatePath());
        assertEquals("queryOrder", result.getData().getToolTemplateModel().getToolName());
        assertEquals("QueryOrderTool", result.getData().getToolTemplateModel().getClassName());
        assertEquals(result.getData(), toolsFolder.getChildren().get(0));
    }

    @Test
    void shouldRejectDuplicateToolFileName() {
        fileService.addHttpTsToolToFolder("QueryOrderTool", "queryOrder", toolsFolder.getId(), "tool-1");

        R<TsHttpToolTemplateFileNode<TsHttpToolTemplateModel>> result =
                fileService.addHttpTsToolToFolder("QueryOrderTool", "queryOrder", toolsFolder.getId(), "tool-1");

        assertEquals(409, result.getCode());
        assertEquals(1, toolsFolder.getChildren().size());
    }

    @Test
    void shouldRejectMissingTool() {
        R<TsHttpToolTemplateFileNode<TsHttpToolTemplateModel>> result =
                fileService.addHttpTsToolToFolder("MissingTool", "missing", toolsFolder.getId(), "missing-id");

        assertEquals(404, result.getCode());
        assertEquals(0, toolsFolder.getChildren().size());
    }
}
