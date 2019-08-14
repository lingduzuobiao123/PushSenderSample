package com.xiaomi.example.pushsender;

import com.alibaba.fastjson.JSONObject;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: dongxl
 * created on: 2019-08-14 11:11
 * description:
 */
public class XiaomiNCMsg {
    private static String APP_SECRET_KEY = "Wrox9tdBU/sjPBktNXLyXg==";
    private static String APP_KEY = "5331717244047";
    private static String appId = "2882303761517172047";//用户在华为开发者联盟申请的appId和appSecret（会员中心->应用管理，点击应用名称的链接）

    public static void main(String[] strs) throws IOException, ParseException {
        Constants.useOfficial();
        String regId = "LepET2v2u7iNIcyKsjLFFfyl3IUjDKL/BRNjDTm7J0owNbLm8ONQe8U3Cm+6XLPm";
        Sender sender = new Sender(APP_SECRET_KEY);
        String messagePayload = getJSONObject().toString()/*URLEncoder.encode(getJSONObject().toString(), "UTF-8")*/;
        String title = " notification title";
        String description = "notification description" + getCurrentTime();
        Message message = new Message.Builder()
                .title(title)
                .description(description)
                .payload(messagePayload)//消息的内容。 透传消息回传给APP, 为必填字段, 非透传消息可选//（注意：需要对payload字符串做urlencode处理）
//                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(-1)     // 以上三种效果都有
                .passThrough(1)  // 1表示透传消息, 0表示通知栏消息(默认是通知栏消息)
//                .extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_ACTIVITY)
                .build();
        Result result = sender.send(message, regId, 3); //根据regID，发送消息到指定设备上
        System.out.println("发送：" + result);
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
