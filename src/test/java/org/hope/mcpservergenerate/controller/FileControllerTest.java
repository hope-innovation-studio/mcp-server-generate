package org.hope.mcpservergenerate.controller;

import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileControllerTest {

    @Test
    void shouldPassClassNameToFileServiceInTheCorrectPosition() {
        FileServiceImpl fileService = mock(FileServiceImpl.class);
        FileController controller = new FileController(fileService);
        when(fileService.addHttpTsToolToFolder(
                "QueryOrderTool",
                "queryOrder",
                "folder-1",
                "tool-1"
        )).thenReturn(R.success(null));

        controller.addHttpTsToolToFolder(
                "queryOrder",
                "folder-1",
                "tool-1",
                "QueryOrderTool"
        );

        verify(fileService).addHttpTsToolToFolder(
                "QueryOrderTool",
                "queryOrder",
                "folder-1",
                "tool-1"
        );
    }
}
