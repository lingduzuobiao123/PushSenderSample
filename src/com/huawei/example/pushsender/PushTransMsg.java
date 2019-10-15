/*
 * FileName:  RedirectMsgService.java
 * License:  Copyright 2014-2024 Huawei Tech. Co. Ltd. All Rights Reserved.
 * Description: Sample of Push Transparent Message Sender
 * Modifier:
 * Modification Date: 2017年7月13日
 * Modification Content: New
 * Modification Version: Push
 */
package com.huawei.example.pushsender;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


//Push透传消息Demo
//本示例程序中的appId,appSecret以及deviceTokens需要用户自行替换为有效值
public class PushTransMsg {
    private static String appSecret = "0e257bbc0f6f84ddf875b488d9150b651ccf0e225e898bb030279d6eb0981dd8";
    private static String appId = "101075517";//用户在华为开发者联盟申请的appId和appSecret（会员中心->应用管理，点击应用名称的链接）
//    private static String tokenUrl = "https://login.vmall.com/oauth2/token";//获取认证Token的URL
    private static String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token";//获取认证Token的URL
    private static String apiUrl = "https://api.push.hicloud.com/pushsend.do"; //应用级消息下发API
    private static String accessToken;//下发通知消息的认证Token
    private static long tokenExpiredTime;  //accessToken的过期时间

    public static void main(String[] args) throws IOException {
//        refreshToken();
        sendPushMessage();
    }

    //获取下发通知消息的认证Token
    private static void refreshToken() throws IOException {
        String msgBody = MessageFormat.format(
                "grant_type=client_credentials&client_secret={0}&client_id={1}",
                URLEncoder.encode(appSecret, "UTF-8"), appId);
        String response = httpPost(tokenUrl, msgBody, 5000, 5000);
        JSONObject obj = JSONObject.parseObject(response);
        accessToken = obj.getString("access_token");
        tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") - 5 * 60 * 1000;
        System.out.println("注册：" + response);
    }

    //发送Push消息
    private static void sendPushMessage() throws IOException {
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            refreshToken();
        }
        /*PushManager.requestToken为客户端申请token的方法，可以调用多次以防止申请token失败*/
        /*PushToken不支持手动编写，需使用客户端的onToken方法获取*/
        JSONArray deviceTokens = new JSONArray();//目标设备Token       
//        deviceTokens.add("AOkqsVa21iVzMWFN71R0Z8yB7X2wpcTSI9T7BYq09BEbHJfM1xQWj-cuCh_x92eoQaTV5DXDPDCeIAnSrAFmW678PpOlT_JGlhJRjWPIsAddYxvjVBxNbNRNcKOo8FqQDQ");
        deviceTokens.add("0860718038683393300004505300CN01");
//        deviceTokens.add("32345678901234561234567890123456");

        JSONObject bodydata = new JSONObject();
        bodydata.put("id", "054ee1e7-ca8a-4210-b4d8-8ff8b08bb2ac1");//透传消息自定义body内容
        JSONObject body = new JSONObject();
        body.put("conv", "054ee1e7-ca8a-4210-b4d8-8ff8b08bb2ac1");//透传消息自定义body内容
        body.put("user", "b61110f5-98db-4076-8853-54d4b64c2eb71");//透传消息自定义body内容
        body.put("type", "notice1");//透传消息自定义body内容
        body.put("data", bodydata);//透传消息自定义body内容

        JSONObject msg = new JSONObject();
        msg.put("type", 1);//1: 透传异步消息，通知栏消息请根据接口文档设置
        msg.put("body", body.toString());//body内容不一定是JSON，可以是String，若为JSON需要转化为String发送

        JSONObject hps = new JSONObject();//华为PUSH消息总结构体
        hps.put("msg", msg);

        JSONObject payload = new JSONObject();
        payload.put("hps", hps);
        System.out.println("透传发送：payload.toString()：" + payload.toString());
        String postBody = MessageFormat.format(
                "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}",
                URLEncoder.encode(accessToken, "UTF-8"),
                URLEncoder.encode("openpush.message.api.send", "UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
                URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
                URLEncoder.encode(payload.toString(), "UTF-8"));
        System.out.println("postBody:" + postBody);
        String postUrl = apiUrl + "?nsp_ctx=" + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + appId + "\"}", "UTF-8");
        System.out.println("postUrl:" + postUrl);
        String response = httpPost(postUrl, postBody, 5000, 5000);
        System.out.println("透传发送：" + response);
    }

    public static String httpPost(String httpUrl, String data, int connectTimeout, int readTimeout) throws IOException {
        OutputStream outPut = null;
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.connect();

            // POST data
            outPut = urlConnection.getOutputStream();
            outPut.write(data.getBytes("UTF-8"));
            outPut.flush();

            // read response
            if (urlConnection.getResponseCode() < 400) {
                in = urlConnection.getInputStream();
            } else {
                in = urlConnection.getErrorStream();
            }

            List<String> lines = IOUtils.readLines(in, urlConnection.getContentEncoding());
            StringBuffer strBuf = new StringBuffer();
            for (String line : lines) {
                strBuf.append(line);
            }
            System.out.println(strBuf.toString());
            return strBuf.toString();
        } finally {
            IOUtils.closeQuietly(outPut);
            IOUtils.closeQuietly(in);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
