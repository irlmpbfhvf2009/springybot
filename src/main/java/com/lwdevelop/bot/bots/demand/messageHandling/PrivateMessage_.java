package com.lwdevelop.bot.bots.demand.messageHandling;

import com.lwdevelop.bot.bots.demand.messageHandling.commands.Demandd;
import com.lwdevelop.bot.bots.telent.messageHandling.commands.Job;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.DemandEnum;
import com.lwdevelop.bot.bots.utils.enum_.TelentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.DemandButton;
import com.lwdevelop.bot.bots.utils.keyboardButton.TelentButton;
import com.lwdevelop.dto.DemandDTO;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.SupplyDTO;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PrivateMessage_ {

    private Common common;
    private Message message;
    private String text;

    private Demandd demandd;
    private Boolean isSubscribeChannel;

    public PrivateMessage_(Common common) {
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.text = this.message.getText();
        this.demandd = new Demandd(new SupplyDTO(common), new DemandDTO(common));
        this.demandd.saveDemandUser(common);
        this.isSubscribeChannel = isSubscribeChannel();
    }

    public void handler() {
        // if (this.text.equals("/start")) {
        // this.setResponse_job();
        // }

        // if (true) {
        if (this.isSubscribeChannel) {
            switch (this.text.toLowerCase()) {
                case "发布需求":
                    this.demandd.setResponse_demand_management(common);
                    break;
                case "发布供应":
                    this.demandd.setResponse_supply_management(common);
                    break;
                case "供应和需求信息管理":
                    this.demandd.setResponse_edit_demand_management(common);
                    this.demandd.setResponse_edit_supply_management(common);
                    break;
                case "/start":
                    this.setResponse_demandd();
                default:
                    break;
            }
            // 判斷事件
            if (text.length() > 4) {
                System.out.println(text);
                String post = text.substring(0, 4);
                // 發布招聘
                if (post.equals(DemandEnum.DEMAND.getText())) {
                    this.demandd.generateTextDemand(common, false);
                } else if (post.equals(DemandEnum.EDIT_DEMAND.getText())) {
                    this.demandd.generateTextDemand(common, true);
                // 發布求職
                } else if (post.equals(DemandEnum.SUPPLY.getText())) {
                    this.demandd.generateTextSupply(common, false);
                } else if (post.equals(DemandEnum.EDIT_SUPPLY.getText())) {
                    this.demandd.generateTextSupply(common, true);
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

        try {
            botName = "@" + this.common.getBot().getMe().getUserName();
        } catch (TelegramApiException e) {
            botName = "";
            e.printStackTrace();
        }

        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(message.getChatId()));
        response.setText(DemandEnum.help_text(name, botName));

        if (this.isSubscribeChannel) {
            response.setReplyMarkup(new DemandButton().demandReplyKeyboardMarkup());
        } else {
            response.setReplyMarkup(new DemandButton().keyboardSubscribeChannelMarkup());
        }
        this.common.executeAsync(response);
    }

    private Boolean isSubscribeChannel() {
        GetChatMember getChatMember = new GetChatMember("-1001784108917", message.getChatId());   //  缅甸招聘频道
//        GetChatMember getChatMember = new GetChatMember("-1001893819364", message.getChatId());  //  测试频道
        Boolean status = this.common.executeAsync(getChatMember);
        return status;
    }

}
