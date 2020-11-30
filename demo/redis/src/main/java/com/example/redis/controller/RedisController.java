package com.example.redis.controller;

import com.example.redis.utils.RedisUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
//miao
@Controller
public class RedisController {
    @Resource
    private RedisUtils redisUtils;

    @ResponseBody
    @RequestMapping("/")
    public String testRedis() {
        redisUtils.set("name", "张三");
        return "success";
    }
}