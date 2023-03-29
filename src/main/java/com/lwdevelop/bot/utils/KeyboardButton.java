package com.lwdevelop.bot.utils;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class KeyboardButton {

    // manage keyboard
    public final ReplyKeyboardMarkup manageReplyKeyboardMarkup() {
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
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public final InlineKeyboardMarkup addToGroupOrChannelMarkupInline(String url, String type) {
        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        dk1.setText("Add to " + type);
        dk1.setUrl(url);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(dk1);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    // job keyboard
    public final ReplyKeyboardMarkup jobReplyKeyboardMarkup(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(SpringyBotEnum.POST_RECRUITMENT.getText());
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SpringyBotEnum.POST_JOBSEARCH.getText());
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(SpringyBotEnum.JOB_MANAGEMENT.getText());
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public final InlineKeyboardMarkup jobFormManagement(String userId,String firstname,String username,String lastname){
        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        dk1.setText("编辑");
        if(firstname==null){
            firstname = "";
        }
        if(username==null){
            username = "";
        }
        if(lastname==null){
            lastname = "";
        }

        String url = "http://192.168.0.67:3002/#/jobSeekerForm?userId="+userId+"&firstname="+firstname+"&username="+username+"&lastname="+lastname;
        dk1.setUrl(url);
        dk2.setText("清除");
        dk2.setUrl("https://yahoo.com.tw");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(dk1);
        rowInline.add(dk2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }


}