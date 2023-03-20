package com.lwdevelop.bot;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

class KeyboardButton {
    protected final ReplyKeyboardMarkup StartReplyKeyboardMarkup(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SpringyBotEnum.HOW_TO_ADD_ME_TO_YOUR_GROUP.getText());
        row.add(SpringyBotEnum.ADMIN_PANEL.getText());
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SpringyBotEnum.HOW_TO_ADD_ME_TO_YOUR_CHANNEL.getText());
        row.add(SpringyBotEnum.SUPPORT_TEAM_LIST.getText());
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SpringyBotEnum.ADMIN_PANEL.getText());
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
