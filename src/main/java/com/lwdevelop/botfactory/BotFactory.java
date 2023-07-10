package com.lwdevelop.botfactory;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import com.lwdevelop.dto.SpringyBotDTO;

public interface BotFactory {
    TelegramLongPollingBot createCoolbaoBot(SpringyBotDTO springyBotDTO);
    TelegramLongPollingBot createTalentBot(SpringyBotDTO springyBotDTO);
    TelegramLongPollingBot createTriSpeakBot(SpringyBotDTO springyBotDTO);
}
