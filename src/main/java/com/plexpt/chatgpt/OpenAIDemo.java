package com.plexpt.chatgpt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OpenAIDemo {
    private static final String OPENAI_API_ENDPOINT = "https://api.xiaoai.plus/v1/images/generations";
    private static final String OPENAI_API_KEY = "sk-pBruIqC3hPClr0qP1c220a2d4294430984A1Bc5429Cc5cE3";

    public static void main(String[] args) {
        String prompt = "生成车水马龙的街道";
        String imageUrl = callOpenAIAndReturnImage(prompt);
        System.out.println("Image URL: " + imageUrl);
    }

    private static String callOpenAIAndReturnImage(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // 连接超时：30秒
                .readTimeout(30, TimeUnit.SECONDS)    // 读取超时：30秒
                .writeTimeout(30, TimeUnit.SECONDS)   // 写入超时：30秒
                .build();
        Gson gson = new Gson();

        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.addProperty("model", "dall-e-3");
        requestBodyJson.addProperty("prompt", prompt);
        requestBodyJson.addProperty("size", "1024x1024");
        RequestBody requestBody = RequestBody.create(requestBodyJson.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(OPENAI_API_ENDPOINT)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
                return responseJson.getAsJsonArray("data").get(0).getAsJsonObject().get("url").getAsString();
            } else {
                System.out.println("OpenAI API request failed with response: " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}