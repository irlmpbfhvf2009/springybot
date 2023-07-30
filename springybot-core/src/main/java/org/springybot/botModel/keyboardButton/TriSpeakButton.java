package org.springybot.botModel.keyboardButton;

import java.util.ArrayList;
import java.util.List;

import org.springybot.botModel.Common;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


public class TriSpeakButton {
    private Common common;

    public TriSpeakButton(Common common) {
        this.common = common;
    }

    public final InlineKeyboardMarkup advertise() {
        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        // List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        // 雲集
        if (this.common.getBotId().equals(5901295307L)) {
            dk1.setText("云集担保流程");
            dk2.setText("供需资源免费发布，将开放机器人");
            dk1.setUrl("https://t.me/yj5999");
            dk2.setUrl("https://t.me/yunji88");
        }

        // ddb37
        if (this.common.getBotId().equals(5822751184L)) {
            dk1.setText("四方支付开户");
            dk2.setText("im聊天系统搭建");
            dk1.setUrl("https://t.me/ddb37/437");
            dk2.setUrl("https://t.me/ddb37/437");
        }

        rowInline1.add(dk1);
        rowInline1.add(dk2);
        rowsInline.add(rowInline1);
        // rowsInline.add(rowInline2);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

}
