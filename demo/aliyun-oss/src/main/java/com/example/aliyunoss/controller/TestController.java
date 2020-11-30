package com.example.aliyunoss.controller;

import com.example.aliyunoss.utils.OSSUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
public class TestController {

    @Resource
    private OSSUtils ossUtils;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("upload")
    public Boolean FileController(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        // objectName 前不要有 /
        String objectName = "user/" + file.getName();
        ossUtils.uploadFile(multipartFile.getBytes(), objectName);
        // 将objectName的值保存到数据库
        return true;
    }

    /**
     * 获取文件地址
     *
     * @return
     */
    @GetMapping("show")
    public String showFile() {
        // objectName的值应该从数据库中读取
        String objectName = "user/logo.png";
        return ossUtils.getTempUrl(objectName);
    }
}
