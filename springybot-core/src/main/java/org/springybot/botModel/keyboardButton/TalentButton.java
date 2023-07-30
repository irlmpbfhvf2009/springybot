package org.springybot.botModel.keyboardButton;

import java.util.ArrayList;
import java.util.List;

import org.springybot.botModel.enum_.TalentEnum;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class TalentButton {

    // job keyboard
    public final ReplyKeyboardMarkup jobReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("发布招聘");
        row.add("发布求职");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(TalentEnum.JOB_MANAGEMENT.getText());
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

    public final InlineKeyboardMarkup keyboard_jobPosting(String userId,String botId, boolean isEdit) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (isEdit) {
            dk1.setText("编辑");
            dk1.setCallbackData("editJobPosting_");
            dk2.setText("删除");
            dk2.setCallbackData("clearJobPosting_" + userId + "_" + botId);
        } else {
            dk1.setText("编辑发布");
            dk1.setCallbackData("editJobPosting_");
        }

        rowInline.add(dk1);
        if (isEdit) {
            rowInline.add(dk2);
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public final InlineKeyboardMarkup keyboard_jobSeeker(String userId,String botId, boolean isEdit) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (isEdit) {
            dk1.setText("编辑");
            dk1.setCallbackData("editJobSeeker_");
            dk2.setText("删除");
            dk2.setCallbackData("clearJobSeeker_" + userId + "_" + botId);
        } else {
            dk1.setCallbackData("editJobSeeker_");
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

    public final InlineKeyboardMarkup keyboard_callme(String username) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        String url = "https://t.me/" + username;
        dk1.setText("联系我");
        dk1.setUrl(url);
        rowInline.add(dk1);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

}