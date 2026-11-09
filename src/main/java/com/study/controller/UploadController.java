package com.study.controller;

import com.study.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    /**
     * 上传目录：项目根目录下的 upload 文件夹。
     * 不再写进 src/main/resources/static，原因有两点：
     * 1. 运行时静态资源从 classpath（target/classes）读取，写进源码目录的文件访问不到；
     * 2. 打成 jar 后源码目录根本不存在，写入必然失败。
     * 这个目录由 WebConfig 中的 addResourceHandlers 映射成 /upload/ 访问路径。
     */
    private static final String UPLOAD_DIR =
            Paths.get(System.getProperty("user.dir"), "upload").toString();

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("上传文件:{}", file.getOriginalFilename());

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            return Result.error("文件名不能为空");
        }

        // 去掉扩展名前先判断是否存在 "."，否则无扩展名时 substring 会抛越界异常
        int dotIndex = filename.lastIndexOf(".");
        String ext = dotIndex >= 0 ? filename.substring(dotIndex) : "";
        String newFileName = UUID.randomUUID().toString().replace("-", "") + ext;

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            log.error("上传目录创建失败: {}", UPLOAD_DIR);
            return Result.error("上传目录创建失败");
        }

        file.transferTo(new File(dir, newFileName));
        log.info("上传成功: {}", newFileName);
        // 返回相对路径，前端拼接后由 WebConfig 的 /upload/** 映射读取
        return Result.success("/upload/" + newFileName);
    }
}
