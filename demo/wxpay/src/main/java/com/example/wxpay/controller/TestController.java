package com.example.wxpay.controller;

import com.example.wxpay.config.WechatConfig;
import com.example.wxpay.sdk.WXPay;
import com.example.wxpay.sdk.WXPayConstants;
import com.example.wxpay.sdk.WXPayUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pay")
public class TestController {

    @Resource
    private WechatConfig wechatConfig;

    /**
     * 打开微信支付二维码
     *
     * @param orderNo 订单编号
     * @return
     */
    @GetMapping("/wxpay/{orderNo}")
    public Map<String, Object> wxPay(@PathVariable String orderNo) {
        Map<String, Object> dto = new HashMap<>();

        // 构建参数
        Map<String, String> reqData = new HashMap<>();
        reqData.put("body", "Java精品课"); // 商品名称
        reqData.put("out_trade_no", orderNo); // 订单号
        reqData.put("total_fee", "1"); // 付款金额，单位为分
        reqData.put("spbill_create_ip", wechatConfig.getSpbillCreateIp());
        reqData.put("notify_url", wechatConfig.getNotifyUrl());
        reqData.put("trade_type", wechatConfig.getTradeType());

        try {
            WXPay wxPay = new WXPay(wechatConfig, wechatConfig.getNotifyUrl(), false, false);
            // 调用统一下单接口
            Map<String, String> stringMap = wxPay.unifiedOrder(reqData);
            // 校验签名
            if (!WXPayUtil.isSignatureValid(stringMap, wechatConfig.getKey(), WXPayConstants.SignType.HMACSHA256)) {
                dto.put("code", 0);
                dto.put("msg", "签名校验失败");
            } else if (!stringMap.get("return_code").equals(WXPayConstants.SUCCESS) || !stringMap.get("result_code").equals(WXPayConstants.SUCCESS)) {
                dto.put("code", 0);
                dto.put("msg", "获取付款二维码失败：" + stringMap.get("err_code_des").split(" ")[1]);
            } else {
                dto.put("code", 1);
                // 获取微信二维码code_url，需要转换成二维码图片
                dto.put("code_url", stringMap.get("code_url"));
            }
        } catch (Exception e) {
            dto.put("code", 0);
            dto.put("msg", e.getMessage());
        }
        return dto;
    }

    /**
     * 用户支付完成后的回调方法
     *
     * @param request
     * @return
     */
    @PostMapping("/wxPay/notify")
    public Map<String, Object> wxPayNotify(HttpServletRequest request) {
        Map<String, Object> dto = new HashMap<>();

        // 接收微信回传信息
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            StringBuilder builder = new StringBuilder();
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String wxParamXml = builder.toString();

            // 签名校验
            if (!WXPayUtil.isSignatureValid(wxParamXml, wechatConfig.getKey())) {
                dto.put("code", 0);
                dto.put("msg", "签名校验失败");
            } else {
                // 将xml字符串转为map对象
                Map wxPayResult = WXPayUtil.xmlToMap(wxParamXml);
                dto.put("code", 1);
                dto.put("data", wxPayResult);
            }
        } catch (Exception e) {
            dto.put("code", 0);
            dto.put("msg", e.getMessage());
        } finally {
            try {
                reader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dto;
    }
}
