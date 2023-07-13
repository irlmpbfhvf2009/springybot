package com.lwdevelop.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import lombok.SneakyThrows;

@Configuration
public class SpringyBotConfig {

    @Bean
    @SneakyThrows
    TelegramBotsApi telegramBotsApi() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        return telegramBotsApi;
    }

    @Bean
    @SneakyThrows
    List<BotSession> botSessions() {
        return new ArrayList<BotSession>();
    }


}
