package com.plexpt.chatgpt.bean;

import com.google.gson.JsonObject;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class BeanComponent {
    @Value("${chatgpt.apiKey}")
    private  String apiKey;
    @Value("${chatgpt.host}")
    private  String apiHost;
    @Bean
    public ChatGPT chatGPTChat() {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(apiKey)
                .apiHost(apiHost) //反向代理地址
                .build()
                .init();
        return chatGPT;
    }
    @Bean
    public ChatCompletion chatGPTChatThree() {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT4o)
                .messages(Arrays.asList(Message.of("用中文")))
                .maxTokens(3000)
                .temperature(0.6)
                .build();
        return chatCompletion;
    }
    //流
    @Bean
    public ChatGPTStream chatGPTStream() {
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .apiKey(apiKey)
                .apiHost(apiHost) //反向代理地址
                .build()
                .init();
        return chatGPTStream;
    }
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // 连接超时：30秒
                .readTimeout(30, TimeUnit.SECONDS)    // 读取超时：30秒
                .writeTimeout(30, TimeUnit.SECONDS)   // 写入超时：30秒
                .build();
        return client;
    }
    @Bean
    public JsonObject jsonObject() {
        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.addProperty("model", "dall-e-3");
        requestBodyJson.addProperty("size", "1024x1024");
        return  requestBodyJson;
    }



}
