package com.lwdevelop.botfactory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import com.lwdevelop.botfactory.bot.coolbao.CoolbaoBot;
import com.lwdevelop.botfactory.bot.telent.TalentBot;
import com.lwdevelop.botfactory.bot.triSpeak.TriSpeakBot;
import com.lwdevelop.dto.SpringyBotDTO;

@Component
public class BotFactoryImpl implements BotFactory {

    @Override
    public TelegramLongPollingBot createCoolbaoBot(SpringyBotDTO springyBotDTO) {
        return new CoolbaoBot(springyBotDTO);
    }

    @Override
    public TelegramLongPollingBot createTalentBot(SpringyBotDTO springyBotDTO) {
        return new TalentBot(springyBotDTO);
    }
    
    @Override
    public TelegramLongPollingBot createTriSpeakBot(SpringyBotDTO springyBotDTO) {
        return new TriSpeakBot(springyBotDTO);
    }
    
    
}
