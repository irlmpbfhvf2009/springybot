package com.lwdevelop.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class CustomWebhookController {

    @PostMapping("/callback/adam")
    public void handleWebhookUpdate(@RequestBody Update update) {
        // System.out.println(update);
        // BotApiMethod<?> response = webhookBot.onWebhookUpdateReceived(update);
        // 处理返回的BotApiMethod对象，例如发送回复消息等操作
    }
}