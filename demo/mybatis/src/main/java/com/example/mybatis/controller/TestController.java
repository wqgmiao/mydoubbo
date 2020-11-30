package com.example.mybatis.controller;

import com.example.mybatis.mapper.TestMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {
    @Resource
    private TestMapper testMapper;

    @RequestMapping("/")
    public Integer test() {
        return testMapper.getCount();
    }
}