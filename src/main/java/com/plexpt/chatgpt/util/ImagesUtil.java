package com.plexpt.chatgpt.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ImagesUtil {
    private static  String IMAGE_URL = "/v1/images/generations";
    @Value("${chatgpt.apiKey}")
    private static String apiKey;
    @Value("${chatgpt.host}")
    private static String apiHost;

    private static OkHttpClient okHttpClient;

    private static JsonObject jsonObject;
    public ImagesUtil(OkHttpClient okHttpClient, JsonObject jsonObject) {
        this.okHttpClient = okHttpClient;
        this.jsonObject = jsonObject;
    }
    public static String createImage(String prompt) {
        Gson gson = new Gson();
        jsonObject.addProperty("prompt", prompt);
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiHost+IMAGE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
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
