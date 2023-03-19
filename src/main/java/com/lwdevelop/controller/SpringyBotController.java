package com.lwdevelop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.lwdevelop.service.impl.SpringyBotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("/springybot")
public class SpringyBotController {

    @Autowired
    private SpringyBotService springyBotService;

    @PostMapping("/t")
    public void getUpdate(@RequestBody Update update) {
        log.info("some update recieved {}",update.toString());
        springyBotService.onWebhookUpdateReceived(update);
    }
    
}
