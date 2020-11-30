package com.example.aliyunssm.controller;

import com.example.aliyunssm.utils.AliyunSSMUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private AliyunSSMUtils aliyunSSMUtils;

    @RequestMapping("/")
    public Boolean test() {
        return aliyunSSMUtils.sendMessage("18952196168", "0000") ? true : false;
    }
}