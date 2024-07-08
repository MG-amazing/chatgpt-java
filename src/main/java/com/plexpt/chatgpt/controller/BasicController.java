package com.plexpt.chatgpt.controller;

import cn.hutool.core.collection.CollUtil;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.constant.Result;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import com.plexpt.chatgpt.util.ImagesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;

import static com.plexpt.chatgpt.constant.Constant.API_HOST;

@RestController
@Slf4j
public class BasicController {
    private final String PATH = API_HOST + "chat_basic";

    private final ChatGPT chatGPT;
    private final ChatCompletion chatCompletion;
    private final ChatGPTStream chatGPTStream;
    private final ImagesUtil imagesUtil;

    public BasicController(ChatGPT chatGPT, ChatCompletion chatCompletion, ChatGPTStream chatGPTStream, ImagesUtil imagesUtil) {
        this.chatGPT = chatGPT;
        this.chatCompletion = chatCompletion;
        this.chatGPTStream = chatGPTStream;
        this.imagesUtil = imagesUtil;
    }


    @GetMapping(PATH)
    private Result<?> getData(@RequestParam String input) {
        String chat = chatGPT.chat(input);
        log.info("chat:{}", chat);
        return Result.OK(chat);
    }

    @GetMapping(PATH + "/turbo")
    private Result<?> getDataTurbo(@RequestParam String input) {
        chatCompletion.setMessages(Arrays.asList(Message.of(input)));
        ChatCompletionResponse chatCompletionResponse = chatGPT.chatCompletion(chatCompletion);
        Message message = null;
        if (CollUtil.isNotEmpty(chatCompletionResponse.getChoices())) {
            message = chatCompletionResponse.getChoices().get(0).getMessage();
        }
        return Result.OK(message==null?"":message.getContent());
    }


    @GetMapping(PATH+"/stream")
    public SseEmitter sseEmitter(@RequestParam String input) {
        //国内需要代理 国外不需要
        SseEmitter sseEmitter = new SseEmitter(-1L);
        chatCompletion.setMessages(Arrays.asList(Message.of(input)));

        SseStreamListener listener = new SseStreamListener(sseEmitter);

        chatGPTStream.streamChatCompletion(chatCompletion, listener);

        return sseEmitter;
    }
    @PostMapping(PATH+"/images")
    public Result<?> images(@RequestParam String input) {
        String image = imagesUtil.createImage(input);
        return Result.OK(image);
    }




}
