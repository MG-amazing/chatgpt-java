package com.plexpt.chatgpt.controller;

import cn.hutool.core.collection.CollUtil;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.constant.Constant;
import com.plexpt.chatgpt.constant.Result;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;

@RestController
@Slf4j
public class BasicController {
    private final String PATH = Constant.API_HOST + "chat_basic";

    private final ChatGPT chatGPT;
    private final ChatCompletion chatCompletion;
    private final ChatGPTStream chatGPTStream;

    public BasicController(ChatGPT chatGPT, ChatCompletion chatCompletion, ChatGPTStream chatGPTStream) {
        this.chatGPT = chatGPT;
        this.chatCompletion = chatCompletion;
        this.chatGPTStream = chatGPTStream;
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
        log.info("chat:{}", chatCompletion.getModel());
        log.info("chat:{}", chatCompletion.countTokens());
        log.info("chat:{}", chatCompletion.getMessages());
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

        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of(input);

        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);


        return sseEmitter;
    }




}
