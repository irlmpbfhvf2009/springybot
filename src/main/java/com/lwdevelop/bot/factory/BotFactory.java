package com.lwdevelop.bot.factory;

import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.bot.model.CustomWebhookBot;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.SpringyBot;

public interface BotFactory {

    CustomLongPollingBot createCoolbaoLongPollingBot(SpringyBot springyBot);
    CustomLongPollingBot createTalentLongPollingBot(SpringyBot springyBot);
    CustomLongPollingBot createTriSpeakLongPollingBot(SpringyBot springyBot);

    CustomWebhookBot createCoolbaoWebhookBot(SpringyBotDTO dto);
    // CustomWebhookBot createTalentWebhookBotBot(SpringyBotDTO dto);
    // CustomWebhookBot createTriSpeakWebhookBotBot(SpringyBotDTO dto);
}
