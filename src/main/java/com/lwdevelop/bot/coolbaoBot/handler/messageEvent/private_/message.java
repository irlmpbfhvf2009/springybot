package com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.commands.addMerchant;
import com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.commands.login;
import com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.commands.start;
import com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.state.enter_password;
import com.lwdevelop.bot.coolbaoBot.utils.Common;
import com.lwdevelop.bot.coolbaoBot.utils.SpringyBotEnum;
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
                case "addMerchant":
                    new addMerchant().am(common);
            }
        }

        switch (this.text.toLowerCase()) {
            case "/start":
                new start().cmd(common);
                break;
            case "/login":
                new login().cmd(common);
                break;
        }

        SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
        Set<WhiteList> whiteList = springyBot.getWhiteList();

        if (whiteList.stream().anyMatch(wl -> wl.getUserId().equals(message.getChatId()))) {
            SendMessage response = new SendMessage();
            response.setChatId(String.valueOf(chatId));
            switch (this.text.toLowerCase()) {
                case "/add_merchant":
                    response.setText("輸入預設定商戶帳號");
                    common.getUserState().put(message.getChatId(), "addMerchant");
                    break;
                case "/info":
                    response.setText(SpringyBotEnum.COMMANDS_INFO.getText());
                    break;
                case "/xxpay":
                    response.setText(SpringyBotEnum.COMMANDS_XXPAY.getText());
                    break;
                case "/sevendays":
                    response.setText(SpringyBotEnum.COMMANDS_SEVENDAYS.getText());
                    break;
                case "/bbippo":
                    response.setText(SpringyBotEnum.COMMANDS_BBIPPO.getText());
                    break;
                case "/help":
                    response.setText("歡迎使用 @" + common.getUsername() + "\n\n" + SpringyBotEnum.COMMANDS_HELP.getText());
                    break;
            }
            if (response.getText() != null) {
                common.sendResponseAsync(response);
            }
        }
    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        // this.common = common;
        this.text = this.message.getText();
    }
}
