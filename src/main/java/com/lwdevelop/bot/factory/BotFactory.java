package com.lwdevelop.bot.factory;

import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.dto.SpringyBotDTO;

public interface BotFactory {
    CustomLongPollingBot createCoolbaoBot(SpringyBotDTO dto);
    CustomLongPollingBot createTalentBot(SpringyBotDTO dto);
    CustomLongPollingBot createTriSpeakBot(SpringyBotDTO dto);
}
