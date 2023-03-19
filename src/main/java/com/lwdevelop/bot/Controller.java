package com.lwdevelop.bot;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/springybot")
public class Controller {

    @Resource
    private TelegramBotsApi telegramBotsApi;

    @Resource
    private DefaultBotOptions defaultBotOptions;

    private BotSession botSession;

    @GetMapping("start")
    private synchronized void startTelegramBot() {
        try {
            if (isBotRunning()){
                stopTelegramBot();
            }
            botSession = telegramBotsApi.registerBot(new Custom(new DefaultBotOptions()));
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
