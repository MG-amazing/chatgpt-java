package com.plexpt.chatgpt.util;

import okhttp3.*;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class ImagesUtil {

    private static final String IMAGE_URL = "/v1/images/generations";

    @Value("${chatgpt.apiKey}")
    private String apiKey;

    @Value("${chatgpt.host}")
    private String apiHost;

    private String fullUrl;

    @PostConstruct
    public void init() {
        fullUrl = apiHost + IMAGE_URL;
    }

    public String createImage(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("model", "dall-e-3");
        jsonObject.addProperty("size", "1024x1024");
        jsonObject.addProperty("prompt", prompt);

        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        System.out.println(fullUrl);  // 调试用

        Request request = new Request.Builder()
                .url(fullUrl)
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
                System.out.println("请求失败: " + response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
