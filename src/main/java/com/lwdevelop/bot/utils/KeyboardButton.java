package com.lwdevelop.bot.utils;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.CryptoUtil;
import com.lwdevelop.utils.SpringUtils;

public class KeyboardButton {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

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
    public final ReplyKeyboardMarkup jobReplyKeyboardMarkup() {
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


    public final InlineKeyboardMarkup keyboard_jobPosting(JobPostingDTO jobPostingDTO) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ub = "userId=" + jobPostingDTO.getUserId()
                + "&botId=" + jobPostingDTO.getBotId()
                + "&company=" + jobPostingDTO.getCompany()
                + "&position=" + jobPostingDTO.getPosition()
                + "&baseSalary=" + jobPostingDTO.getBaseSalary()
                + "&commission=" + jobPostingDTO.getCommission()
                + "&workTime=" + jobPostingDTO.getWorkTime()
                + "&requirements=" + jobPostingDTO.getRequirements()
                + "&location=" + jobPostingDTO.getLocation()
                + "&flightNumber=" + jobPostingDTO.getFlightNumber();

        String encryptedUb = CryptoUtil.encrypt(ub);
        String encodedUb = URLEncoder.encode(encryptedUb, StandardCharsets.UTF_8);
        String url = "http://" + ip + ":3002/#/jobPostingForm?ub=" + encodedUb;
        dk1.setText("编辑");
        dk1.setUrl(url);
        dk2.setText("清除");
        dk2.setCallbackData("clearJobPosting_" + jobPostingDTO.getUserId());
        rowInline.add(dk1);
        rowInline.add(dk2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    
    public final InlineKeyboardMarkup keyboard_jobSeeker(JobSeekerDTO jobSeekerDTO) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ub = "userId=" + jobPostingDTO.getUserId()
                + "&botId=" + jobPostingDTO.getBotId()
                + "&company=" + jobPostingDTO.getCompany()
                + "&position=" + jobPostingDTO.getPosition()
                + "&baseSalary=" + jobPostingDTO.getBaseSalary()
                + "&commission=" + jobPostingDTO.getCommission()
                + "&workTime=" + jobPostingDTO.getWorkTime()
                + "&requirements=" + jobPostingDTO.getRequirements()
                + "&location=" + jobPostingDTO.getLocation()
                + "&flightNumber=" + jobPostingDTO.getFlightNumber();

        String encryptedUb = CryptoUtil.encrypt(ub);
        String encodedUb = URLEncoder.encode(encryptedUb, StandardCharsets.UTF_8);
        String url = "http://" + ip + ":3002/#/jobPostingForm?ub=" + encodedUb;
        dk1.setText("编辑");
        dk1.setUrl(url);
        dk2.setText("清除");
        dk2.setCallbackData("clearJobPosting_" + jobPostingDTO.getUserId());
        rowInline.add(dk1);
        rowInline.add(dk2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

}