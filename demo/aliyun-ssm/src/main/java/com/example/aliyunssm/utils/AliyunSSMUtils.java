package com.example.aliyunssm.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.example.aliyunssm.config.AliyunSSMConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 短信发送工具类
 */
@Component
public class AliyunSSMUtils {

    @Resource
    private AliyunSSMConfig aliyunSSMConfig;

    /**
     * 发送短信的方法
     *
     * @param phone 接收短信的手机号
     * @param code  验证码
     * @return
     */
    public boolean sendMessage(String phone, String code) {

        DefaultProfile profile = DefaultProfile.getProfile(aliyunSSMConfig.getRegionId(), aliyunSSMConfig.getAccessKeyId(), aliyunSSMConfig.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(aliyunSSMConfig.getDomain());
        request.setSysVersion(aliyunSSMConfig.getVersion());
        request.setSysAction(aliyunSSMConfig.getAction());
        request.putQueryParameter("RegionId", aliyunSSMConfig.getRegionId());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", aliyunSSMConfig.getSignName());
        request.putQueryParameter("TemplateCode", aliyunSSMConfig.getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());

            String status = JSONObject.parseObject(response.getData()).get("Code").toString();

            return "OK".equals(status);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return false;
    }
}