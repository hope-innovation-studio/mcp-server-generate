package org.hope.mcpservergenerate.service;

import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.model.tree.FolderNode;
import org.hope.mcpservergenerate.model.tree.httptemplatenode.TsHttpStaticTemplateFileNode;
import org.hope.mcpservergenerate.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileServiceImplTest {

    private FolderNode root;
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        root = new FolderNode("default");
        fileService = new FileServiceImpl(new HttpFileTreeContext(root));
    }

    @Test
    void shouldAddFolderAndReturnCreatedNode() {
        R<FolderNode> result = fileService.addFolder(root.getId(), "src");

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("default/src", result.getData().getPath());
        assertEquals(result.getData(), root.getChildren().get(0));
    }

    @Test
    void shouldRejectDuplicateNodeNameInSameFolder() {
        root.add(new FolderNode("default/src"));

        R<FolderNode> result = fileService.addFolder(root.getId(), "src");

        assertEquals(409, result.getCode());
        assertEquals(1, root.getChildren().size());
    }

    @Test
    void shouldRejectFileAsParent() {
        TsHttpStaticTemplateFileNode file = new TsHttpStaticTemplateFileNode("default/index.ts");
        root.add(file);

        R<FolderNode> result = fileService.addFolder(file.getId(), "child");

        assertEquals(400, result.getCode());
    }

    @Test
    void shouldRejectBlankFolderName() {
        R<FolderNode> result = fileService.addFolder(root.getId(), "  ");

        assertEquals(400, result.getCode());
        assertEquals(0, root.getChildren().size());
    }
}
