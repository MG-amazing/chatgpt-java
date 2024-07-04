package com.plexpt.chatgpt.controller;

import cn.hutool.core.collection.CollUtil;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.constant.Constant;
import com.plexpt.chatgpt.constant.Result;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.entity.images.ImagesRensponse;
import com.plexpt.chatgpt.entity.images.Variations;
import com.plexpt.chatgpt.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class BasicController {
    private final String PATH = Constant.API_HOST + "chat_basic";

    private final ChatGPT chatGPT;
    private final ChatCompletion chatCompletion;

    public BasicController(ChatGPT chatGPT, ChatCompletion chatCompletion) {
        this.chatGPT = chatGPT;
        this.chatCompletion = chatCompletion;
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

    @PostMapping(PATH + "/image")
    private Result<?> getDataImage(@RequestParam MultipartFile file) throws IOException {
        Variations variations = Variations.ofURL(1, "256x256");
        File file1 = FileUtil.convertToFile(file);

        ImagesRensponse imagesRensponse = chatGPT.imageVariation(file1, variations);

        List<Object> data = imagesRensponse.getData();
        for (Object o : data) {
            System.out.println(o.toString());
        }

        return Result.OK(imagesRensponse);
    }



}
