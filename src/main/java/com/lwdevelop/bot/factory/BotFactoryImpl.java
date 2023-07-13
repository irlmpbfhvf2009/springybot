package com.lwdevelop.bot.factory;

import org.springframework.stereotype.Component;
import com.lwdevelop.bot.bots.coolbao.CoolbaoBot;
import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.bot.bots.telent.TalentBot;
import com.lwdevelop.bot.bots.triSpeak.TriSpeakBot;
import com.lwdevelop.dto.SpringyBotDTO;

@Component
public class BotFactoryImpl implements BotFactory {

    @Override
    public CustomLongPollingBot createCoolbaoBot(SpringyBotDTO dto) {
        return new CoolbaoBot(dto);
    }

    @Override
    public CustomLongPollingBot createTalentBot(SpringyBotDTO dto) {
        return new TalentBot(dto);
    }
    
    @Override
    public CustomLongPollingBot createTriSpeakBot(SpringyBotDTO dto) {
        return new TriSpeakBot(dto);
    }
    
    
}
