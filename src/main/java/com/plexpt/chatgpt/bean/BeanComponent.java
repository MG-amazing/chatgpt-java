package com.plexpt.chatgpt.bean;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.entity.images.Generations;
import com.plexpt.chatgpt.entity.images.ImagesRensponse;
import com.unfbx.chatgpt.entity.images.Image;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BeanComponent {
    @Bean
    public ChatGPT chatGPTChat() {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-pBruIqC3hPClr0qP1c220a2d4294430984A1Bc5429Cc5cE3")
                .apiHost("https://api.xiaoai.plus") //反向代理地址
                .build()
                .init();
        return chatGPT;
    }
    @Bean
    public ChatCompletion chatGPTChatThree() {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO)
                .messages(Arrays.asList(Message.of("用中文")))
                .maxTokens(3000)
                .temperature(0.6)
                .build();
        return chatCompletion;
    }


}
