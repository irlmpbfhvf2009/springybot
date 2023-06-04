package com.lwdevelop.bot.triSpeak.utils;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.lwdevelop.bot.Common;

public class KeyboardButton {
    private Common common;

    public KeyboardButton(Common common) {
        this.common = common;
    }

    public final InlineKeyboardMarkup advertise() {
        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        // 雲集
        if (this.common.getBotId().equals(SpringyBotEnum.YUNJI_ID.getId())) {
            dk1.setText(SpringyBotEnum.YUNJI_DK1.getText());
            dk2.setText(SpringyBotEnum.YUNJI_DK2.getText());
            dk1.setUrl(SpringyBotEnum.YUNJI_URL1.getText());
            dk2.setUrl(SpringyBotEnum.YUNJI_URL2.getText());
        }

        // ddb37
        if (this.common.getBotId().equals(SpringyBotEnum.DDB37_ID.getId())) {
            dk1.setText(SpringyBotEnum.DDB37_DK1.getText());
            dk2.setText(SpringyBotEnum.DDB37_DK2.getText());
            dk1.setUrl(SpringyBotEnum.DDB37_URL1.getText());
            dk2.setUrl(SpringyBotEnum.DDB37_URL2.getText());
        }

        rowInline1.add(dk1);
        rowInline2.add(dk2);
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

}
