package com.lwdevelop.bot.factory;

import org.springframework.stereotype.Component;
import com.lwdevelop.bot.bots.coolbao.CoolbaoLongPollingBot;
import com.lwdevelop.bot.bots.coolbao.CoolbaoWebhookBot;
import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.bot.model.CustomWebhookBot;
import com.lwdevelop.bot.bots.triSpeak.TriSpeakLongPollingBot;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.SpringyBot;

@Component
public class BotFactoryImpl implements BotFactory {
    

    @Override
    public CustomLongPollingBot createCoolbaoLongPollingBot(SpringyBot springyBot) {
        return new CoolbaoLongPollingBot(springyBot);
    }

    @Override
    public CustomLongPollingBot createTalentLongPollingBot(SpringyBot springyBot) {
        return new TriSpeakLongPollingBot(springyBot);
    }

    @Override
    public CustomLongPollingBot createTriSpeakLongPollingBot(SpringyBot springyBot) {
        return new TriSpeakLongPollingBot(springyBot);
    }

    @Override
    public CustomWebhookBot createCoolbaoWebhookBot(SpringyBotDTO dto) {
        return new CoolbaoWebhookBot(dto);
    }

}
