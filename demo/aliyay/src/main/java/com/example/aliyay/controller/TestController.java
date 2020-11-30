package com.example.aliyay.controller;

import com.example.aliyay.utils.AliPayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
//miao
@Controller
@RequestMapping("/api/pay")
public class TestController {

    @Resource
    private AliPayUtils aliPayUtils;

    /**
     * 跳转到支付页
     *
     * @param subject
     * @param payno
     * @param amount
     * @return
     */
    @ResponseBody
    @PostMapping("/alipay")
    public String aliPay(String subject, String payno, String amount) {
        return aliPayUtils.toPcPay(subject, payno, amount);
    }

    /**
     * 支付成功回调函数
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/returnDeal")
    public String returnDeal(HttpServletRequest request) throws Exception {
        // 获取支付宝响应参数
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> paramters = new HashMap<>();
        params.entrySet();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            paramters.put(entry.getKey(), entry.getValue()[0]);
        }

        // 验签
        boolean flag = aliPayUtils.verify(paramters);
        if (!flag) {
            return "fail";
        }

        return "redirect:" + aliPayUtils.getAliPayConfig().getSuccessUrl();
    }
}
