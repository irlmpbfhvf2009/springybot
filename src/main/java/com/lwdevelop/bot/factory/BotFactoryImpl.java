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
    public CustomLongPollingBot createCoolbaoBot(SpringyBotDTO springyBotDTO) {
        return new CoolbaoBot(springyBotDTO);
    }

    @Override
    public CustomLongPollingBot createTalentBot(SpringyBotDTO springyBotDTO) {
        return new TalentBot(springyBotDTO);
    }
    
    @Override
    public CustomLongPollingBot createTriSpeakBot(SpringyBotDTO springyBotDTO) {
        return new TriSpeakBot(springyBotDTO);
    }
    
    
}
