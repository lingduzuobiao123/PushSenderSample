package com.meizu.example.pushsender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.ErrorCode;
import com.meizu.push.sdk.server.constant.PushResponseCode;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author: dongxl
 * created on: 2019-08-13 10:39
 * description:
 */
public class MeizuNcMsg {
    private static String APP_SECRET_KEY = "b7db1ebdd88f4b9ab289609fe4f04628";
    private static long appId = 123037;//用户在华为开发者联盟申请的appId和appSecret（会员中心->应用管理，点击应用名称的链接）

    public static void main(String[] args) throws IOException {
//        refreshToken();
        sendPushMessage();
    }


    private static void sendPushMessage() throws IOException {
//推送对象
        IFlymePush push = new IFlymePush(APP_SECRET_KEY, true);
        List<String> pushIds = new ArrayList<>();//推送目标，一批最多不能超过1000个
        pushIds.add("S5Q4b72627b4b6f797d5e435d030304505c4177677f45");

        //组装消息
        VarnishedMessage message = new VarnishedMessage.Builder().appId(appId)
                .title("Java SDK 推送标题").content("Java SDK 推送内容")
                .noticeExpandType(1)
                .noticeExpandContent("展开文本内容:" + getCurrentTime())
                .clickType(0)
//                .url("http://www.baidu.com")
                .parameters(getJSONObject())
                .offLine(true)
                .validTime(12)
                .suspend(true)
                .clearNoticeBar(true)
                .vibrate(true).lights(true)
                .sound(true)
                .build();
        ResultPack<PushResult> result = push.pushMessage(message, pushIds);
        if (result.isSucceed()) {
// 2 调用推送服务成功 (其中map为设备的具体推送结果，一般业务针对超速的 code类型做处理)
            PushResult pushResult = result.value();
            String msgId = pushResult.getMsgId();//推送消息ID，用于推送流程明细排查
            Map<String, List<String>> targetResultMap = pushResult.getRespTarget();//推送结果，全部推送成功，则map为empty
            System.out.println("push msgId:" + msgId);
            System.out.println("push targetResultMap:" + targetResultMap);
            if (targetResultMap != null && !targetResultMap.isEmpty()) {
// 3 判断是否有获取超速的target
                if (targetResultMap.containsKey(PushResponseCode.RSP_SPEED_LIMIT.getValue())) {
// 4 获取超速的target
                    List<String> rateLimitTarget = targetResultMap.get(PushResponseCode.RSP_SPEED_LIMIT.getValue());
                    System.out.println("rateLimitTarget is :" + rateLimitTarget);//TODO 5 业务处理，重推......
                }
            }
        } else {
// 调用推送接口服务异常 eg: appId、appKey非法、推送消息非法..... // result.code(); //服务异常码
// result.comment();//服务异常描述
//全部超速
            if (String.valueOf(ErrorCode.APP_REQUEST_EXCEED_LIMIT.getValue()).equals(result.code())) {
//TODO 5 业务处理，重推...... }
                System.out.println(String.format("pushMessage error code:%s comment:%s", result.code(), result.comment()));
            }
        }
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
