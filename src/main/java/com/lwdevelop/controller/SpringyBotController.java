package com.lwdevelop.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

import com.lwdevelop.bot.Custom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/springybot")
public class SpringyBotController {

    @Resource
    private TelegramBotsApi telegramBotsApi;


    private BotSession botSession;

    @GetMapping("start")
    private synchronized void startTelegramBot() {
        try {
            if (isBotRunning()){
                stopTelegramBot();
            }
            String token = "5855785269:AAH9bvPpYudd2wSAvMnBTiKakCeoB92_Z_8";
            String username = "CCP1121_BOT";
            botSession = telegramBotsApi.registerBot(new Custom(token,username,new DefaultBotOptions()));
            log.info("Common Telegram bot started.");
        } catch (TelegramApiException e) {
            log.error("Catch TelegramApiException", e);
        }
    }

    @GetMapping("stop")
    private void stopTelegramBot() {
        try {
            if (botSession != null) {
                botSession.stop();
                botSession = null;
            }
        } catch (Exception e) {
            log.error("Catch exception", e);
        }
    }

    public boolean isBotRunning() {
        return botSession != null && botSession.isRunning();
    }

}
