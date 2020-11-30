package com.example.wxlogin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wxlogin.config.WechatConfig;
import com.example.wxlogin.utils.UrlUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {

    @Resource
    private WechatConfig wechatConfig;

    /**
     * 显示微信登陆扫码界面
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/wechat/login")
    public void wechat(HttpServletResponse response) throws Exception {
        StringBuffer wxsb = new StringBuffer(wechatConfig.getOpenUrl());
        wxsb.append("?appid=" + wechatConfig.getAppid());
        wxsb.append("&redirect_uri=" + wechatConfig.getRedirectUri());
        wxsb.append("&response_type=" + wechatConfig.getResponseType());
        wxsb.append("&scope=" + wechatConfig.getScope());
        wxsb.append("&state=" + wechatConfig.getState());
        response.sendRedirect(wxsb.toString());
    }

    /**
     * 用户手机确认后回调函数
     *
     * @param code
     * @throws Exception
     */
    @RequestMapping("/wechat/callback")
    public Object callback(String code) throws Exception {
        // 构造请求URL
        StringBuffer wxsb = new StringBuffer(wechatConfig.getAccessTokenUrl());
        wxsb.append("?appid=" + wechatConfig.getAppid());
        wxsb.append("&secret=" + wechatConfig.getSecret());
        wxsb.append("&code=" + code);
        wxsb.append("&grant_type=" + wechatConfig.getGrantType());

        // 发送请求并获取accessToken和opendId
        String resp = UrlUtils.loadURL(wxsb.toString());
        JSONObject jsonObject = JSONObject.parseObject(resp);
        String accessToken = jsonObject.getString("access_token");
        String openId = jsonObject.getString("openId");

        // 构造获取用户信息的URL
        StringBuffer usb = new StringBuffer(wechatConfig.getUserInfoUrl());
        usb.append("?access_token=" + accessToken);
        usb.append("&openid=" + openId);

        // 发送请求并获取用户信息
        String userInfo = UrlUtils.loadURL(usb.toString());
        JSONObject userObject = JSONObject.parseObject(userInfo);

        return userObject;
    }
}
