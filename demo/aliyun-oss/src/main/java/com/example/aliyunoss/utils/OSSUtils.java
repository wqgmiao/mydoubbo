package com.example.aliyunoss.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.example.aliyunoss.config.OSSConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云OSS服务器工具类
 */
@Component
public class OSSUtils {

    @Resource
    private OSSConfig ossConfig;

    /**
     * 文件上传
     *
     * @param bytes      要上传文件的字节流
     * @param objectName 目录名/文件名
     */
    public void uploadFile(byte[] bytes, String objectName) {
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), objectName, new ByteArrayInputStream(bytes));
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
    }

    /**
     * 获取图片的临时URL
     *
     * @param objectName 目录名/文件名
     * @return
     */
    public String getTempUrl(String objectName) {
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        URL url = ossClient.generatePresignedUrl(ossConfig.getBucketName(), objectName, expiration);
        ossClient.shutdown();
        return url.toString();
    }
}
