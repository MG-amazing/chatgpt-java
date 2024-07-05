package com.plexpt.chatgpt.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ImagesUtil {
    private static String IMAGE_URL = "/v1/images/generations";
    @Value("${chatgpt.apiKey}")
    private static String apiKey;
    @Value("${chatgpt.host}")
    private static String apiHost;


    public static String createImage(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // 连接超时：30秒
                .readTimeout(30, TimeUnit.SECONDS)    // 读取超时：30秒
                .writeTimeout(30, TimeUnit.SECONDS)   // 写入超时：30秒
                .build();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("model", "dall-e-3");
        jsonObject.addProperty("size", "1024x1024");
        Gson gson = new Gson();
        jsonObject.addProperty("prompt", prompt);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Request request = new Request.Builder()
                .url(apiHost + IMAGE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
                return responseJson.getAsJsonArray("data").get(0).getAsJsonObject().get("url").getAsString();
            } else {
                log.info("请求失败: " + response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

}
