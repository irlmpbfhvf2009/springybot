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
import com.lwdevelop.entity.JobPosting;
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

    public final InlineKeyboardMarkup jobFormManagement(Common common, String path,JobPosting jobPosting,JobSeeker jobSeeker) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
        String firstname = common.getUpdate().getMessage().getChat().getFirstName();
        String username = common.getUpdate().getMessage().getChat().getUserName();
        String lastname = common.getUpdate().getMessage().getChat().getLastName();

        if (firstname == null) {
            firstname = "";
        }
        if (username == null) {
            username = "";
        }
        if (lastname == null) {
            lastname = "";
        }
        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        JobUser jobUser = new JobUser();
        jobUser.setFirstname(firstname);
        jobUser.setLastname(lastname);
        jobUser.setUserId(userId);
        jobUser.setUsername(username);
        springyBot.getJobUser().stream()
                .filter(j -> j.getUserId().equals(userId))
                .findFirst()
                .ifPresentOrElse(j -> {
                }, () -> {
                    springyBot.getJobUser().add(jobUser);
                    springyBotServiceImpl.save(springyBot);
                });

        String botId = String.valueOf(common.getSpringyBotId());
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if(path.equals("jobPostingForm")){
            String company = ""; // 公司名称
            String position = ""; // 职位名称
            String baseSalary = ""; // 底薪
            String commission = ""; // 提成
            String workTime = ""; // 上班时间
            String requirements = ""; // 要求内容
            String location = ""; // 地址
            String flightNumber = ""; // 咨询飞机号
    
            if (jobPosting != null) {
                company = jobPosting.getCompany();
                position = jobPosting.getPosition();
                baseSalary = jobPosting.getBaseSalary();
                commission = jobPosting.getCommission();
                workTime = jobPosting.getWorkTime();
                requirements = jobPosting.getRequirements();
                location = jobPosting.getLocation();
                flightNumber = jobPosting.getFlightNumber();
            }

            String ub = "userId=" + userId 
                                + "&botId=" + botId 
                                + "&company=" + company 
                                + "&position=" + position 
                                + "&baseSalary=" + baseSalary 
                                + "&commission=" + commission 
                                + "&workTime=" + workTime 
                                + "&requirements=" + requirements 
                                + "&location=" + location 
                                + "&flightNumber=" + flightNumber;

            String encryptedUb = CryptoUtil.encrypt(ub);
            String encodedStr = URLEncoder.encode(encryptedUb, StandardCharsets.UTF_8);
            String url = "http://" + ip + ":3002/#/" + path + "?ub=" + encodedStr;
            dk1.setText("编辑");
            dk1.setUrl(url);
            dk2.setText("清除");
            // dk2.setUrl("https://yahoo.com.tw");
            dk2.setCallbackData("clearJobPosting_"+userId);
            rowInline.add(dk1);
            rowInline.add(dk2);
            rowsInline.add(rowInline);
            markupInline.setKeyboard(rowsInline);
        }else if(path.equals("jobSeekerForm")){
            String ub = "userId=" + userId 
                                + "&botId=" + botId;

            String encryptedUb = CryptoUtil.encrypt(ub);
            String url = "http://" + ip + ":3002/#/" + path + "?ub=" + encryptedUb;
            dk1.setText("编辑");
            dk1.setUrl(url);
            dk2.setText("清除");
            dk2.setUrl("https://yahoo.com.tw");
            rowInline.add(dk1);
            rowInline.add(dk2);
            rowsInline.add(rowInline);
            markupInline.setKeyboard(rowsInline);
        }
        return markupInline;
    }

}