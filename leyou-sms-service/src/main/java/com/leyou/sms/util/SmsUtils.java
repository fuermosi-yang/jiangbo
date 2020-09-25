package com.leyou.sms.util;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.leyou.sms.config.SmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Title: SmsUtils
 * @Description: 发送短信工具类
 * @Auther:
 * @Version: 1.0
 * @create 2020/8/16
 */
@Component
public class SmsUtils {

    /**
     <dependency>
     <groupId>com.aliyun</groupId>
     <artifactId>aliyun-java-sdk-core</artifactId>
     <version>4.5.3</version>
     </dependency>

     /
     /**
     *
     * @param phone 收短信手机号
     * @param code  验证码
     */
    public static void sendSms(String phone,String code){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4GJpCBY5eYxdhsuB39RC", "wjSHBoRlKGFQsqXQUfT71E3SoExWvR");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "leyougou");
        request.putQueryParameter("TemplateCode", "SMS_199797497");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
