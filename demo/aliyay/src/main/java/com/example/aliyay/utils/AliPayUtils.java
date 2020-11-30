package com.example.aliyay.utils;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.factory.Factory.Payment;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.example.aliyay.config.AliPayConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 阿里支付工具类
 */
@Component
public class AliPayUtils {

    @Resource
    private AliPayConfig aliPayConfig;

    public AliPayConfig getAliPayConfig() {
        return aliPayConfig;
    }

    /**
     * 支付
     *
     * @param sunject 项目名称
     * @param payNo   订单号
     * @param amount  支付金额
     * @return
     */
    public String toPcPay(String sunject, String payNo, String amount) {
        String result = "";
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions());
        try {
            // 2. 发起API调用
            AlipayTradePagePayResponse response = Payment.Page().pay(sunject, payNo, amount, aliPayConfig.getReturnUrl());
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                result = response.body;
                System.out.println("调用成功");
            } else {
                System.err.println("调用失败，原因：" + response.body);
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        return result;
    }

    private Config getOptions() {
        Config config = new Config();
        config.protocol = aliPayConfig.getProtocol();
        config.gatewayHost = aliPayConfig.getGatewayHost();
        config.signType = aliPayConfig.getSignType();
        config.appId = aliPayConfig.getAppId();
        config.merchantPrivateKey = aliPayConfig.getMerchantPrivateKey();
        config.encryptKey = aliPayConfig.getEncryptKey();
        config.notifyUrl = aliPayConfig.getNotifyUrl();
        config.alipayPublicKey = aliPayConfig.getAlipayPublicKey();
        return config;
    }

    /**
     * 验签
     *
     * @param parameters
     * @return
     * @throws Exception
     */
    public Boolean verify(Map<String, String> parameters) throws Exception {
        return Factory.Payment.Common().verifyNotify(parameters);
    }
}