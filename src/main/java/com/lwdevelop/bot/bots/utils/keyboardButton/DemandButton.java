package com.lwdevelop.bot.bots.utils.keyboardButton;

import com.lwdevelop.bot.bots.utils.enum_.DemandEnum;
import com.lwdevelop.bot.bots.utils.enum_.TelentEnum;
import com.lwdevelop.dto.DemandDTO;
import com.lwdevelop.dto.SupplyDTO;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

public class DemandButton {

    // manage keyboard
    public final ReplyKeyboardMarkup manageReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(TelentEnum.HOW_TO_ADD_ME_TO_YOUR_GROUP.getText());
        row.add(TelentEnum.ADMIN_PANEL.getText());
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(TelentEnum.HOW_TO_ADD_ME_TO_YOUR_CHANNEL.getText());
        row.add(TelentEnum.SUPPORT_TEAM_LIST.getText());
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

    // demand keyboard
    public final ReplyKeyboardMarkup demandReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(DemandEnum.POST_DEMAND.getText());
        row.add(DemandEnum.POST_SUPPLY.getText());
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
        dk1.setText(TelentEnum.subscribeChannel_text());
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

    public final InlineKeyboardMarkup keyboard_demand(DemandDTO demandDTO, boolean isEdit) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        // String ub = "userId=" + jobPostingDTO.getUserId()
        // + "&botId=" + jobPostingDTO.getBotId()
        // + "&company=" + jobPostingDTO.getCompany()
        // + "&position=" + jobPostingDTO.getPosition()
        // + "&baseSalary=" + jobPostingDTO.getBaseSalary()
        // + "&commission=" + jobPostingDTO.getCommission()
        // + "&workTime=" + jobPostingDTO.getWorkTime()
        // + "&requirements=" + jobPostingDTO.getRequirements()
        // + "&location=" + jobPostingDTO.getLocation()
        // + "&flightNumber=" + jobPostingDTO.getFlightNumber();

        // String encryptedUb = CryptoUtil.encrypt(ub);
        // String encodedUb = URLEncoder.encode(encryptedUb, StandardCharsets.UTF_8);
        // String url;

        if (isEdit) {
            // url = "http://192.168.0.27:3000/#/edit_jobPostingForm?ub=" + encodedUb;
            // url = "http://rc.ddb99.vip:18889/#/jobPostingForm?ub=" + encodedUb;
            dk1.setText("编辑");
            dk1.setCallbackData(DemandEnum.EDIT_DEMAND_CQ.getText());
            dk2.setText("删除");
            // dk2.setCallbackData("clearJobPosting_" + jobPostingDTO.getUserId());
            dk2.setCallbackData(DemandEnum.CLEAR_DEMAND_CQ.getText() + demandDTO.getUserId() + "_" + demandDTO.getBotId());
        } else {
            // url = "http://192.168.0.27:3000/#/jobPostingForm?ub=" + encodedUb;
            // url = "http://rc.ddb99.vip:18889/#/jobPostingForm?ub=" + encodedUb;
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

    public final InlineKeyboardMarkup keyboard_supply(SupplyDTO supplyDTO, boolean isEdit) {

        InlineKeyboardButton dk1 = new InlineKeyboardButton();
        InlineKeyboardButton dk2 = new InlineKeyboardButton();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        // String ub = "userId=" + jobSeekerDTO.getUserId()
        // + "&botId=" + jobSeekerDTO.getBotId()
        // + "&name=" + jobSeekerDTO.getName()
        // + "&gender=" + jobSeekerDTO.getGender()
        // + "&dateOfBirth=" + jobSeekerDTO.getDateOfBirth()
        // + "&age=" + jobSeekerDTO.getAge()
        // + "&nationality=" + jobSeekerDTO.getNationality()
        // + "&education=" + jobSeekerDTO.getEducation()
        // + "&skills=" + jobSeekerDTO.getSkills()
        // + "&targetPosition=" + jobSeekerDTO.getTargetPosition()
        // + "&resources=" + jobSeekerDTO.getResources()
        // + "&expectedSalary=" + jobSeekerDTO.getExpectedSalary()
        // + "&workExperience=" + jobSeekerDTO.getWorkExperience()
        // + "&selfIntroduction=" + jobSeekerDTO.getSelfIntroduction()
        // + "&flightNumber=" + jobSeekerDTO.getFlightNumber();

        // String encryptedUb = CryptoUtil.encrypt(ub);
        // String encodedUb = URLEncoder.encode(encryptedUb, StandardCharsets.UTF_8);
        // String url;

        if (isEdit) {
            // url = "http://192.168.0.27:3000/#/edit_jobSeekerForm?ub=" + encodedUb;
            // url = "http://rc.ddb99.vip:18889/#/jobSeekerForm?ub=" + encodedUb;
            dk1.setText("编辑");
            dk1.setCallbackData(DemandEnum.EDIT_SUPPLY_CQ.getText());
            dk2.setText("删除");
            // dk2.setCallbackData("clearJobSeeker_" + jobSeekerDTO.getUserId());
            dk2.setCallbackData(DemandEnum.CLEAR_SUPPLY_CQ.getText() + supplyDTO.getUserId() + "_" + supplyDTO.getBotId());
        } else {
            // url = "http://192.168.0.27:3000/#/jobSeekerForm?ub=" + encodedUb;
            // url = "http://rc.ddb99.vip:18889/#/jobSeekerForm?ub=" + encodedUb;\
            dk1.setCallbackData(DemandEnum.EDIT_SUPPLY_CQ.getText());
            dk1.setText("编辑发布");
        }

        // dk1.setUrl(url);

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