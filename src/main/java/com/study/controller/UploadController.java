package com.study.controller;

import com.study.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("上传文件:{} ",file);
        String filename=file.getOriginalFilename();
        String newfileName= UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."));
        file.transferTo(new File("D:/Software/IDEA/Java_code/wep-ai-project02/tlias-wep-mangement/src/main/resources/static/upload/"+newfileName));
        return Result.success();
    }
}
