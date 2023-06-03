package com.lwdevelop.bot.triSpeak.utils;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class KeyboardButton {
    public final InlineKeyboardMarkup ddb37_url() {
        
        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        dk1.setText("四方支付开户");
        dk2.setText("im聊天系统搭建");
        dk1.setUrl("https://t.me/ddb37/437");
        dk2.setUrl("https://t.me/ddb37/437");
        rowInline1.add(dk1);
        rowInline2.add(dk2);
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

}
