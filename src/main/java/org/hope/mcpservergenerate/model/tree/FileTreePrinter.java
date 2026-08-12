package org.hope.mcpservergenerate.model.tree;

import java.nio.file.Path;
import java.util.List;

/**
 * 将文件节点树转换为便于在控制台查看的文本。
 */
public final class FileTreePrinter {

    private FileTreePrinter() {
    }

    public static String print(FileSystemNode root) {
        StringBuilder result = new StringBuilder();
        result.append(nameOf(root)).append(System.lineSeparator());

        if (root instanceof FolderNode folder) {
            appendChildren(folder.getChildren(), "", result);
        }

        return result.toString();
    }

    private static void appendChildren(
            List<FileSystemNode> children,
            String prefix,
            StringBuilder result
    ) {
        for (int index = 0; index < children.size(); index++) {
            FileSystemNode child = children.get(index);
            boolean last = index == children.size() - 1;

            result.append(prefix)
                    .append(last ? "└── " : "├── ")
                    .append(nameOf(child))
                    .append(System.lineSeparator());

            if (child instanceof FolderNode folder) {
                appendChildren(
                        folder.getChildren(),
                        prefix + (last ? "    " : "│   "),
                        result
                );
            }
        }
    }

    private static String nameOf(FileSystemNode node) {
        String path = node.getPath();
        if (path == null || path.isBlank()) {
            return "(未命名)";
        }

        Path fileName = Path.of(path).getFileName();
        return fileName == null ? path : fileName.toString();
    }
}
