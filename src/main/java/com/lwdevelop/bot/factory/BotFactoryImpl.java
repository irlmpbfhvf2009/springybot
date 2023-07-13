package com.lwdevelop.bot.factory;

import org.springframework.stereotype.Component;
import com.lwdevelop.bot.bots.coolbao.CoolbaoLongPollingBot;
import com.lwdevelop.bot.bots.coolbao.CoolbaoWebhookBot;
import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.bot.model.CustomWebhookBot;
import com.lwdevelop.bot.bots.triSpeak.TriSpeakLongPollingBot;
import com.lwdevelop.dto.SpringyBotDTO;

@Component
public class BotFactoryImpl implements BotFactory {
    

    @Override
    public CustomLongPollingBot createCoolbaoLongPollingBot(SpringyBotDTO dto) {
        return new CoolbaoLongPollingBot(dto);
    }

    @Override
    public CustomLongPollingBot createTalentLongPollingBot(SpringyBotDTO dto) {
        return new TriSpeakLongPollingBot(dto);
    }

    @Override
    public CustomLongPollingBot createTriSpeakLongPollingBot(SpringyBotDTO dto) {
        return new TriSpeakLongPollingBot(dto);
    }

    @Override
    public CustomWebhookBot createCoolbaoWebhookBot(SpringyBotDTO dto) {
        return new CoolbaoWebhookBot(dto);
    }

}
