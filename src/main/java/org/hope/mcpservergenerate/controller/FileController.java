package org.hope.mcpservergenerate.controller;

import lombok.RequiredArgsConstructor;
import org.hope.mcpservergenerate.model.R;
import org.hope.mcpservergenerate.service.impl.FileServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 关岁安
 * @since 2026/8/12
 */
@RestController
@RequestMapping("/generate")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;

    /**
     * 在系统文件夹中新添加一个文件夹
     * @param parentId 父文件id
     * @param pathName 路径名字
     * @return
     */
    @PostMapping("add-folder")
    public R<String> addFolder(String parentId, String pathName){
        return fileService.addFolder(parentId, pathName);
    }

}
