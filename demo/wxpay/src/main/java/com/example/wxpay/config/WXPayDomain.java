package com.example.wxpay.config;

import com.example.wxpay.sdk.IWXPayDomain;
import com.example.wxpay.sdk.WXPayConfig;
import com.example.wxpay.sdk.WXPayConstants;
import org.springframework.stereotype.Component;

@Component
public class WXPayDomain implements IWXPayDomain {
    @Override
    public void report(String domain, long elapsedTimeMillis, Exception ex) {

    }

    @Override
    public DomainInfo getDomain(WXPayConfig config) {
        return new DomainInfo(WXPayConstants.DOMAIN_API, true);
    }
}
