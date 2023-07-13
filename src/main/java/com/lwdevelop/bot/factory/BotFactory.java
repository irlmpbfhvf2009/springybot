package com.lwdevelop.bot.factory;

import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.bot.model.CustomWebhookBot;
import com.lwdevelop.dto.SpringyBotDTO;

public interface BotFactory {

    CustomLongPollingBot createCoolbaoLongPollingBot(SpringyBotDTO dto);
    CustomLongPollingBot createTalentLongPollingBot(SpringyBotDTO dto);
    CustomLongPollingBot createTriSpeakLongPollingBot(SpringyBotDTO dto);

    CustomWebhookBot createCoolbaoWebhookBot(SpringyBotDTO dto);
    // CustomWebhookBot createTalentWebhookBotBot(SpringyBotDTO dto);
    // CustomWebhookBot createTriSpeakWebhookBotBot(SpringyBotDTO dto);
}
