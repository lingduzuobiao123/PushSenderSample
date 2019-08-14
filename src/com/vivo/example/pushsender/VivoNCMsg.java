package com.vivo.example.pushsender;

import com.alibaba.fastjson.JSONObject;
import com.vivo.push.sdk.notofication.Message;
import com.vivo.push.sdk.notofication.Result;
import com.vivo.push.sdk.server.Sender;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: dongxl
 * created on: 2019-08-14 11:03
 * description:
 */
public class VivoNCMsg {
    private static String APP_SECRET_KEY = "5e109c17-627a-436b-b30f-55feaff1c23d";
    private static String APP_KEY = "6ab44aea-3c2a-4dd9-94c7-e0e5d0cb4d26";
    private static int appId = 14449;//用户在华为开发者联盟申请的appId和appSecret（会员中心->应用管理，点击应用名称的链接）

    public static void main(String[] strs) throws Exception {
        Sender sender = new Sender(APP_SECRET_KEY);//注册登录开发平台网站获取到的appSecret
        Result result = sender.getToken(appId, APP_KEY);//注册登录开发平台网站获取到的appId和appKey
        Sender senderMessage = new Sender(APP_SECRET_KEY, result.getAuthToken());
        Message singleMessage = new Message.Builder()
                .regId("regId")//该测试手机设备订阅推送后生成的regId
                .notifyType(4)
                .title("推送标题")
                .content("推送内容"+getCurrentTime())
                .timeToLive(1000)
                .skipType(1)
                .skipContent("http://www.vivo.com")
                .networkType(-1)
                .requestId("1234567890123456")
                .build();
        Result resultMessage = senderMessage.sendSingle(singleMessage);
        System.out.println(resultMessage);
    }

    private static JSONObject getJSONObject() {
        JSONObject bodydata = new JSONObject();
        bodydata.put("id", "054ee1e7-ca8a-4210-b4d8-8ff8b08bb2ac1");//透传消息自定义body内容
        JSONObject body = new JSONObject();
        body.put("conv", "054ee1e7-ca8a-4210-b4d8-8ff8b08bb2ac1");//透传消息自定义body内容
        body.put("user", "b61110f5-98db-4076-8853-54d4b64c2eb71");//透传消息自定义body内容
        body.put("type", "notice1");//透传消息自定义body内容
        body.put("data", bodydata);//透传消息自定义body内容
        return body;
    }

    private static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }
}
