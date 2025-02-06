package com.zhoujh.lvtu.utils;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HuaweiPushService {

    private static final String AUTH_URL = "https://oauth-login.cloud.huawei.com/oauth2/v3/token";
    private static final String PUSH_URL = "https://push-api.cloud.huawei.com/v1/113393177/messages:send";
    private static final String CLIENT_ID = "113393177";
    private static final String CLIENT_SECRET = "79513052b4734c75b354806206bbf73cf5686145c3e6a554aa39aef989f2a7aa";


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

    public void sendPushMessage(String accessToken, String deviceToken, String message) throws IOException {
        OkHttpClient client = new OkHttpClient();

//        String json = "{\n" +
//                "    \"validate_only\": false,\n" +
//                "    \"message\": {\n" +
//                "        \"android\": {\n" +
//                "            \"notification\": {\n" +
//                "                \"title\": \"test title\",\n" +
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
                "        \"data\": \"{'message':'"+message+"','param2':'value2'}\",\n" +
                "        \"token\": [\""+deviceToken+"\"]\n" +
                "    }\n" +
                "}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(PUSH_URL)
                .post(body)
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
