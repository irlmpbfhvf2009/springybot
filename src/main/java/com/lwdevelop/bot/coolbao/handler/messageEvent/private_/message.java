package com.lwdevelop.bot.coolbao.handler.messageEvent.private_;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.coolbao.handler.messageEvent.private_.commands.addMerchant;
import com.lwdevelop.bot.coolbao.handler.messageEvent.private_.commands.cgBalance;
import com.lwdevelop.bot.coolbao.handler.messageEvent.private_.commands.login;
import com.lwdevelop.bot.coolbao.handler.messageEvent.private_.commands.start;
import com.lwdevelop.bot.coolbao.handler.messageEvent.private_.state.enter_name;
import com.lwdevelop.bot.coolbao.handler.messageEvent.private_.state.enter_password;
import com.lwdevelop.bot.Common;
import com.lwdevelop.bot.coolbao.utils.SpringyBotEnum;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class message {
    private Message message;
    private String text;

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    public void handler(Common common) {
        this.init(common);

        Long chatId = common.getUpdate().getMessage().getChatId();
        String state = common.getUserState().get(chatId);

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

            SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
            Set<WhiteList> whiteList = springyBot.getWhiteList();

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
                            response.setText(
                                    "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
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
                    case "/info":
                        response.setText(SpringyBotEnum.COMMANDS_INFO.getText());
                        break;
                    case "/xxpay":
                        response.setText(SpringyBotEnum.COMMANDS_XXPAY.getText());
                        common.executeAsync(response);
                            response.setText(
                                    "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
                        break;
                    case "/sevendays":
                        response.setText(SpringyBotEnum.COMMANDS_SEVENDAYS.getText());
                        common.executeAsync(response);
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(String.valueOf(chatId));
                        String fileId = "AgACAgUAAxkBAAIoj2RbOJRM-e6bIs7pNQbBcY4a9uA5AAIYtjEbO9PYVhYO1zjC8iI5AQADAgADcwADLwQ";
                        InputFile inputFile = new InputFile(fileId);
                        sendPhoto.setPhoto(inputFile);
                        common.executeAsync(sendPhoto);
                            response.setText(
                                    "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
                        break;
                    case "/bbippo":
                        response.setText(SpringyBotEnum.COMMANDS_BBIPPO.getText());
                        common.executeAsync(response);
                            response.setText(
                                    "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
                        break;
                    case "/dahe":
                        response.setText(SpringyBotEnum.COMMANDS_DAHE.getText());
                        common.executeAsync(response);
                            response.setText(
                                    "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
                        break;
                    case "/white_dove":
                        response.setText(SpringyBotEnum.COMMANDS_WHITEDOVE.getText());
                        common.executeAsync(response);
                            response.setText(
                                    "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
                        break;
                    case "/help":
                        response.setText(
                                "歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
                        break;
                }
                if (response.getText() != null) {
                    common.executeAsync(response);
                }
            }

        }

    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        // this.common = common;
        this.text = this.message.getText();
    }

    private static String formatCurrentDateWithWeekday(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d（E）", Locale.TAIWAN);
        return date.format(formatter);
    }
}
