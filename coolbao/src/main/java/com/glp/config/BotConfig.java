package com.glp.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BotConfig {

    @Autowired
    RestTemplate restTemplate;

    @Value("${telegram.botToken}")
    private String botToken;

    @Value("${telegram.botUsername}")
    private String botUsername;

    @Bean
    CoolbaoLongPollingBot coolbaoLongPollingBot() {
        DefaultBotOptions options = new DefaultBotOptions();
        options.setAllowedUpdates(Arrays.asList("update_id", "message", "edited_message",
                "channel_post", "edited_channel_post", "inline_query", "chosen_inline_result",
                "callback_query", "shipping_query", "pre_checkout_query", "poll", "poll_answer",
                "my_chat_member", "chat_member"));

        CoolbaoLongPollingBot bot = new CoolbaoLongPollingBot(options, botUsername, botToken);
        try {
            String url = "http://localhost:5488/springybot/v1/getBot";
            String springyBot = restTemplate.postForObject(url, botToken, String.class);
            log.info("springyBot");
            log.info(springyBot);
            log.info("springyBot");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bot;
    }

    @Bean
    TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(coolbaoLongPollingBot());
        return telegramBotsApi;
    }

}