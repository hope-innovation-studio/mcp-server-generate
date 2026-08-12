package org.hope.mcpservergenerate.model.tree;

import org.hope.mcpservergenerate.model.tree.httptemplatenode.TsHttpStaticTemplateFileNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileTreePrinterTest {

    @Test
    void printsFrameworkTree() {
        FolderNode root = new FolderNode("mcp-server");
        FolderNode src = new FolderNode("src");
        FolderNode framework = new FolderNode("src/framework");
        FolderNode client = new FolderNode("src/framework/client");
        FolderNode decorator = new FolderNode("src/framework/decorator");
        FolderNode interfaces = new FolderNode("src/framework/interface");
        FolderNode load = new FolderNode("src/framework/load");

        root.add(src);
        src.add(framework);
        framework.add(client);
        framework.add(decorator);
        framework.add(interfaces);
        framework.add(load);

        client.add(new TsHttpStaticTemplateFileNode(
                "src/framework/client/http-client.ts",
                "src/framework/client/http-client.ts"
        ));
        decorator.add(new TsHttpStaticTemplateFileNode(
                "src/framework/decorator/tool-register.decorator.ts",
                "src/framework/decorator/tool-register.decorator.ts"
        ));
        interfaces.add(new TsHttpStaticTemplateFileNode(
                "src/framework/interface/response.interface.ts",
                "src/framework/interface/response.interface.ts"
        ));
        interfaces.add(new TsHttpStaticTemplateFileNode(
                "src/framework/interface/system.interface.ts",
                "src/framework/interface/system.interface.ts"
        ));
        interfaces.add(new TsHttpStaticTemplateFileNode(
                "src/framework/interface/tool.interface.ts",
                "src/framework/interface/tool.interface.ts"
        ));
        load.add(new TsHttpStaticTemplateFileNode(
                "src/framework/load/load-config.ts",
                "src/framework/load/load-config.ts"
        ));
        load.add(new TsHttpStaticTemplateFileNode(
                "src/framework/load/load-tool.ts",
                "src/framework/load/load-tool.ts"
        ));

        String tree = FileTreePrinter.print(root);
        System.out.println(tree);

        assertTrue(tree.contains("framework"));
        assertTrue(tree.contains("http-client.ts"));
        assertTrue(tree.contains("load-tool.ts"));
    }
}
