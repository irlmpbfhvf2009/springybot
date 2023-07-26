package com.lwdevelop.bot.factory;

import com.lwdevelop.bot.bots.demand.DemandLongPollingBot;
import org.springframework.stereotype.Component;

import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.bot.model.CustomWebhookBot;
import com.lwdevelop.bot.bots.coolbao.CoolbaoLongPollingBot;
import com.lwdevelop.bot.bots.coolbao.CoolbaoWebhookBot;
import com.lwdevelop.bot.bots.talent.TalentLongPollingBot;
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
        return new TalentLongPollingBot(dto);
    }

    @Override
    public CustomLongPollingBot createTriSpeakLongPollingBot(SpringyBotDTO dto) {
        return new TriSpeakLongPollingBot(dto);
    }

    @Override
    public CustomLongPollingBot createDemandLongPollingBot(SpringyBotDTO dto) {
        return new DemandLongPollingBot(dto);
    }

    @Override
    public CustomWebhookBot createCoolbaoWebhookBot(SpringyBotDTO dto) {
        return new CoolbaoWebhookBot(dto);
    }

}
