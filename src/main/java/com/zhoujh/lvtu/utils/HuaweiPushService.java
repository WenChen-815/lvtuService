package com.zhoujh.lvtu.utils;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class HuaweiPushService {

    private static final String AUTH_URL = "https://oauth-login.cloud.huawei.com/oauth2/v3/token";
    private static final String PUSH_URL = "https://push-api.cloud.huawei.com/v1/{app_id}/messages:send";
    private static final String CLIENT_ID = "113389873";
    private static final String CLIENT_SECRET = "70a0c8a50142848f3f14cce9d1a0083464ff0efa413cd21665a6b8445cf907a8";


    public String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .build();

        Request request = new Request.Builder()
                .url(AUTH_URL)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            // 解析JSON获取access_token
            return parseAccessToken(responseBody);
        }
    }
    private String parseAccessToken(String json) {
        // 使用JSON库解析access_token
        return new com.google.gson.JsonParser().parse(json).getAsJsonObject().get("access_token").getAsString();
    }

    public void sendPushMessage(String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String accessToken = getAccessToken();

        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(PUSH_URL.replace("{app_id}", CLIENT_ID))
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            if (response.body() != null) {
                System.out.println(response.body().string());
            }
        }
    }

    public void sendPushMessage1(String deviceToken, String title, String message) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String accessToken = getAccessToken();
//        String json = "{\n" +
//                "    \"validate_only\": false,\n" +
//                "    \"message\": {\n" +
//                "        \"android\": {\n" +
//                "            \"notification\": {\n" +
//                "                \"title\": \""+title+"\",\n" +
//                "                \"body\": \""+message+"\",\n" +
//                "                \"foreground_show\": false,\n" +
//                "                \"click_action\": {\n" +
//                "                    \"type\": 3\n" +
//                "                }\n" +
//                "            }\n" +
//                "        },\n" +
//                "        \"token\": [\""+deviceToken+"\"]\n" +
//                "    }\n" +
//                "}";
        String json = "{\n" +
                "    \"validate_only\": false,\n" +
                "    \"message\": {\n" +
                "        \"data\": \"{'message':'"+message+"','title':'"+title+"'}\",\n" +
                "        \"token\": [\""+deviceToken+"\"]\n" +
                "    }\n" +
                "}";
//        String json = "{\n" +
//                "    \"validate_only\": false,\n" +
//                "    \"message\": {\n" +
//                "        \"notification\": {\n" +
//                "            \"title\": \"旅兔通知\",\n" +
//                "            \"body\": \"有旅友想要参加你的旅行计划，请及时联系哦~\"\n" +
//                "        },\n" +
//                "        \"android\": {\n" +
//                "            \"notification\": {\n" +
//                "                \"foreground_show\": true,\n" +
//                "                \"click_action\": {\n" +
//                "                    \"type\": 1,\n" +
//                "                    \"intent\": \"intent://com.zhoujh.lvtu/planDisplay?#Intent;scheme=lvtu;launchFlags=0x04000000;i.age=180;S.name=abc;S.travelPlanId="+travelPlan.getTravelPlanId()+";end\"\n" +
//                "                }\n" +
//                "            }\n" +
//                "        },\n" +
//                "        \"data\": \"{'travelPlanId':'"+travelPlan.getTravelPlanId()+"'}\",\n" +
//                "        \"token\": [\n" +
//                "            "+ sb.toString() +"\n" +
//                "        ]\n" +
//                "    }\n" +
//                "}";

        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(PUSH_URL.replace("{app_id}", CLIENT_ID))
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            if (response.body() != null) {
                System.out.println(response.body().string());
            }
        }
    }
}
