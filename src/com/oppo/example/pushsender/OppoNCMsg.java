package com.oppo.example.pushsender;

import com.alibaba.fastjson.JSONObject;
import com.oppo.push.server.Notification;
import com.oppo.push.server.Result;
import com.oppo.push.server.Sender;
import com.oppo.push.server.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * author: dongxl
 * created on: 2019-08-14 11:11
 * description:
 */
public class OppoNCMsg {
    private static String APP_SECRET_KEY = "5e109c17-627a-436b-b30f-55feaff1c23d";
    private static String APP_KEY = "6ab44aea-3c2a-4dd9-94c7-e0e5d0cb4d26";
    private static String appId = "14449";//用户在华为开发者联盟申请的appId和appSecret（会员中心->应用管理，点击应用名称的链接）

    public static void main(String[] strs) throws Exception {
        Sender sender = new Sender(APP_KEY, APP_SECRET_KEY);
        Target target = Target.build("CN_8fa0618f178145d8c2a44091a1326411"); //创建发送对象
        Notification notification = getNotification(); //创建通知栏消息体
        Result resultMessage = sender.unicastNotification(notification, target);  //发送单推消息
        System.out.println(resultMessage);
    }

    private static Notification getNotification() {
        Notification notification = new Notification();

/**
 * 以下参数必填项
 */

        notification.setTitle("通知栏消息tile");

        notification.setSubTitle("sub tile");

        notification.setContent("通知栏内容"+getNotification());

/**
 * 以下参数非必填项， 如果需要使用可以参考OPPO push服务端api文档进行设置

 */

// App开发者自定义消息Id，OPPO推送平台根据此ID做去重处理，对于广播推送相同appMessageId只会保存一次，对于单推相同appMessageId只会推送一次

        notification.setAppMessageId(UUID.randomUUID().toString());

// 应用接收消息到达回执的回调URL，字数限制200以内，中英文均以一个计算

        notification.setCallBackUrl("http://www.test.com");

// App开发者自定义回执参数，字数限制50以内，中英文均以一个计算

        notification.setCallBackParameter("");

// 点击动作类型0，启动应用；1，打开应用内页（activity的intent action）；2，打开网页；4，打开应用内页（activity）；【非必填，默认值为0】;5,Intent scheme URL

        notification.setClickActionType(0);

// 应用内页地址【click_action_type为1或4时必填，长度500】

        notification.setClickActionActivity("com.coloros.push.demo.component.InternalActivity");

// 网页地址【click_action_type为2必填，长度500】

        notification.setClickActionUrl("http://www.test.com");

// 动作参数，打开应用内页或网页时传递给应用或网页【JSON格式，非必填】，字符数不能超过4K，示例：{"key1":"value1","key2":"value2"}

        notification.setActionParameters("{\"key1\":\"value1\",\"key2\":\"value2\"}");

// 展示类型 (0, “即时”),(1, “定时”)

        notification.setShowTimeType(1);

// 定时展示开始时间（根据time_zone转换成当地时间），时间的毫秒数

        notification.setShowStartTime(System.currentTimeMillis() + 1000 * 60 * 3);

// 定时展示结束时间（根据time_zone转换成当地时间），时间的毫秒数

        notification.setShowEndTime(System.currentTimeMillis() + 1000 * 60 * 5);

// 是否进离线消息,【非必填，默认为True】

        notification.setOffLine(true);

// 离线消息的存活时间(time_to_live) (单位：秒), 【off_line值为true时，必填，最长3天】

        notification.setOffLineTtl(24 * 3600);

// 时区，默认值：（GMT+08:00）北京，香港，新加坡

        notification.setTimeZone("GMT+08:00");

// 0：不限联网方式, 1：仅wifi推送

        notification.setNetworkType(0);

        return notification;
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
