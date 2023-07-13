package com.lwdevelop.bot.bots.coolbao.messageHandling;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.addMerchant;
import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.cgBalance;
import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.enter_name;
import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.enter_password;
import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.login;
import com.lwdevelop.bot.bots.coolbao.messageHandling.commands.start;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.CoolbaoEnum;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class PrivateMessage_ {

    private Message message;
    private String text;
    private Common common;
    private User user;
    private Long chatId;
    private String state;
    private String botUsername;

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public PrivateMessage_(Common common) {
        this.common = common;
        this.message = common.getUpdate().getMessage();
        this.text = common.getUpdate().getMessage().getText();
        this.chatId = common.getUpdate().getMessage().getChatId();
        this.state = common.getUserState().get(chatId);
        this.user = common.getUpdate().getMessage().getFrom();
        this.botUsername = common.getBotUsername();
    }

    public void handler() {

        if (StringUtils.hasText(state)) {
            switch (state) {
                case "enter_password":
                    new enter_password().ep(common);
                    break;
                case "addMerchant":
                    new addMerchant().am(common);
                    break;
                case "enter_name":
                    new enter_name().en(common);
                    break;
            }
        } else {
            switch (this.text.toLowerCase()) {
                case "/start":
                    new start().cmd(common);
                    break;
                case "/id_card":
                    new login().cmd(common);
                    break;
            }

            List<WhiteList> whiteList = springyBotServiceImpl.findWhiteListBySpringyBotId(common.getSpringyBotId());

            if (whiteList.stream().anyMatch(wl -> wl.getUserId().equals(message.getChatId()))) {
                SendMessage response = new SendMessage();
                response.setChatId(String.valueOf(chatId));
                switch (this.text.toLowerCase()) {
                    case "/punch_in":
                        LocalDate currentDate = LocalDate.now();
                        String dateString = formatCurrentDateWithWeekday(currentDate);
                        whiteList.stream().filter(wl -> wl.getUserId().equals(chatId)).findAny().ifPresent(action -> {
                            response.setText(dateString + action.getName() + "值班 10:00-19:00");
                            common.executeAsync(response);
                            response.setText(dateString + action.getName() + "值班 13:00-22:00");
                            common.executeAsync(response);
                            response.setText(CoolbaoEnum.commandsHelp(this.botUsername, user));
                            response.setParseMode("HTML");
                            response.setDisableWebPagePreview(true);
                        });
                        ;
                        break;
                    case "/cg_balance":
                        response.setText(new cgBalance().cmd(common));
                        break;
                    case "/add_merchant":
                        response.setText("輸入預設定商戶帳號 /quit - 退出模式");
                        common.getUserState().put(message.getChatId(), "addMerchant");
                        break;
                    case "/help":
                        response.setText(CoolbaoEnum.commandsHelp(this.botUsername, user));
                        response.setParseMode("HTML");
                        response.setDisableWebPagePreview(true);
                        break;
                }
                if (response.getText() != null) {
                    common.executeAsync(response);
                }
            }

        }

    }

    private static String formatCurrentDateWithWeekday(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d（E）", Locale.TAIWAN);
        String str = date.format(formatter);
        str = str.replace("週", "");
        return str;
    }
}
