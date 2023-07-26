package com.lwdevelop.bot.bots.demand.messageHandling;

import com.lwdevelop.bot.bots.demand.messageHandling.commands.Demandd;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.DemandEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.DemandButton;
import com.lwdevelop.bot.chatMessageHandlers.BasePrivateMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PrivateMessage_ extends BasePrivateMessage {

    private Demandd demandd;

    public PrivateMessage_(Common common) {
        super(common);
        this.demandd = new Demandd(common);
    }

    public void handler() {

        Boolean isSubscribeChannel = isSubscribeChannel();

        if (isSubscribeChannel) {

            switch (this.text.toLowerCase()) {
                case "发布需求":
                    this.demandd.setResponse_demand_management();
                    break;
                case "发布供应":
                    this.demandd.setResponse_supply_management();
                    break;
                case "供需信息管理":
                    this.demandd.setResponse_edit_demand_management();
                    this.demandd.setResponse_edit_supply_management();
                    break;
                case "/start":
                    this.setResponse_demandd();
                default:
                    break;
            }

            // 判斷事件
            if (text.length() > 2) {
                String post = text.substring(0, 2);
                // 發布招聘
                if (post.equals("需求")) {
                    this.demandd.generateTextDemand(false);
                } else if (post.equals("编辑需求")) {
                    this.demandd.generateTextDemand(true);
                    // 發布求職
                } else if (post.equals("供应")) {
                    this.demandd.generateTextSupply(false);
                } else if (post.equals("编辑供应")) {
                    this.demandd.generateTextSupply(true);
                }
            }

        } else {
            this.setResponse_demandd();
        }
    }

    private void setResponse_demandd() {

        String firstName = this.message.getFrom().getFirstName() == null ? "" : this.message.getFrom().getFirstName();
        String lastName = this.message.getFrom().getLastName() == null ? "" : this.message.getFrom().getLastName();
        String name = firstName + lastName;
        String botName;
        Boolean isSubscribeChannel = isSubscribeChannel();

        try {
            botName = "@" + this.common.getBot().getMe().getUserName();
        } catch (TelegramApiException e) {
            botName = "";
            e.printStackTrace();
        }

        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(message.getChatId()));
        response.setText(DemandEnum.help_text(name, botName));

        if (isSubscribeChannel) {
            response.setReplyMarkup(new DemandButton().demandReplyKeyboardMarkup());
        } else {
            response.setReplyMarkup(new DemandButton().keyboardSubscribeChannelMarkup());
        }
        this.common.executeAsync(response);
    }

    private Boolean isSubscribeChannel() {
        GetChatMember getChatMember = new GetChatMember("-1001686617172", message.getChatId()); // 缅甸招聘频道
        Boolean status = this.common.executeAsync(getChatMember);
        // return true;
        return status;
    }

}
