package com.lwdevelop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
public class TelegramBotController {

    @PostMapping("/your_bot_path")
    public void handleTelegramWebhook(@RequestBody Update update) {
        System.out.println(update);
    }
    
}
