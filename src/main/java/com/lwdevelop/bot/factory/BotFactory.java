package com.lwdevelop.bot.factory;

import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.dto.SpringyBotDTO;

public interface BotFactory {
    CustomLongPollingBot createCoolbaoBot(SpringyBotDTO springyBotDTO);
    CustomLongPollingBot createTalentBot(SpringyBotDTO springyBotDTO);
    CustomLongPollingBot createTriSpeakBot(SpringyBotDTO springyBotDTO);
}
