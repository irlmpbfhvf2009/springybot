package org.springybot.botModel.keyboardButton;

import org.springybot.botModel.enum_.DemandEnum;
import org.springybot.botModel.enum_.TalentEnum;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

public class DemandButton {


    // demand keyboard
    public final ReplyKeyboardMarkup demandReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("发布需求");
        row.add("发布供应");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(DemandEnum.SUPPLY_AND_DEMAND_MANAGEMENT.getText());
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public final InlineKeyboardMarkup keyboardSubscribeChannelMarkup() {
        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        dk1.setText(TalentEnum.subscribeChannel_text());
        dk1.setUrl("https://t.me/rc499");
        rowInline.add(dk1);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public final InlineKeyboardMarkup keyboardJobMarkup() {
        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        dk1.setText("缅甸亚泰水沟谷人才");
        dk1.setUrl("https://t.me/ok799");
        dk2.setText("亚太御龙湾资源");
        dk2.setUrl("https://t.me/yt669");
        rowInline.add(dk1);
        rowInline.add(dk2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public final InlineKeyboardMarkup keyboard_demand(String userId,String botId,  boolean isEdit) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (isEdit) {
            dk1.setText("编辑");
            dk1.setCallbackData("editDemand_");
            dk2.setText("删除");
            dk2.setCallbackData("clearDemand_" + userId + "_" + botId);
        } else {
            dk1.setText("编辑发布");
            dk1.setCallbackData("editDemand_");
        }

        rowInline.add(dk1);
        if (isEdit) {
            rowInline.add(dk2);
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public final InlineKeyboardMarkup keyboard_supply(String userId,String botId,  boolean isEdit) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (isEdit) {
            dk1.setText("编辑");
            dk1.setCallbackData("editSupply_");
            dk2.setText("删除");
            dk2.setCallbackData("clearSupply_" + userId + "_" + botId);
        } else {
            dk1.setCallbackData("editSupply_");
            dk1.setText("编辑发布");
        }

        rowInline.add(dk1);
        if (isEdit) {
            rowInline.add(dk2);
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

}