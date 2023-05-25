package com.lwdevelop.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.lwdevelop.bot.test.TelegrambotWebhook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/callback")
public class TelegramBotController {

    private TelegrambotWebhook telegrambot;
    
    public TelegramBotController(TelegrambotWebhook telegrambot) {
        this.telegrambot = telegrambot;
    }

    @PostMapping("/adam")
    public void getUpdateWithDifferentUrl(@RequestBody Update update){
        log.info("some update recieved ");

        telegrambot.onWebhookUpdateReceived(update);

    }
}
