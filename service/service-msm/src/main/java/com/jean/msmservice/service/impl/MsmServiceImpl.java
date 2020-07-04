package com.jean.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.jean.msmservice.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    //发送短信的方法
    @Override
    public boolean send(Map<String, Object> param, String phone) {

        if(StringUtils.isEmpty(phone)) return false;

        DefaultProfile profile =
        DefaultProfile.getProfile("default", "LTAI4GAsEY5HwsS2R56jZc8W", "oGaEcWLBYXB76dlJcQd5NC9AWvIdDa");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置相关固定的参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        //请求阿里云的哪个服务
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        //请求服务里的发送短信方法
        request.setAction("SendSms");

        //设置发送的相关的参数
        request.putQueryParameter("PhoneNumbers",phone); //手机号
        request.putQueryParameter("SignName", "jean的在线教育视频网"); //阿里云短信服务中签名管理的签名名称
        request.putQueryParameter("TemplateCode", "SMS_193249802");//阿里云短信服务中模板管理的模板CODE
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); //验证码数据，需要转换成json数据进行传递给阿里云

       try {
           //最终发送
           CommonResponse response = client.getCommonResponse(request);
           boolean success = response.getHttpResponse().isSuccess();
           return success;
       }
       catch(Exception e){
           e.printStackTrace();
           return false;
       }

    }
}
