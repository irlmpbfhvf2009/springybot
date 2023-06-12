package com.lwdevelop.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/callback")
public class WebhooksController {

    @PostMapping("/adam")
    public void getUpdateWithDifferentUrl(@RequestBody Update update){
        System.out.println(update);
        log.info("some update recieved ");

        // telegrambot.onWebhookUpdateReceived(update);
    }
}
