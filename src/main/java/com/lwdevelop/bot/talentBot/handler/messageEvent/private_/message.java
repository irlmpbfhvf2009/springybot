package com.lwdevelop.bot.talentBot.handler.messageEvent.private_;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.talentBot.handler.messageEvent.private_.commands.Job_II;
import com.lwdevelop.bot.talentBot.utils.Common;
import com.lwdevelop.bot.talentBot.utils.KeyboardButton;

public class message {

    private Common common;
    private Message message;
    private String text;
    private SendMessage response;

    public void handler(Common common) {
        this.init(common);

        // 判斷事件
        if(text.length()>=4){
            String post = text.substring(0, 4);
            // 發布招聘
            if (post.equals("招聘人才")) {
                new Job_II().generateTextJobPosting(common,false);
            }else if (post.equals("编辑招聘")){
                new Job_II().generateTextJobPosting(common,true);
            }
    
            // 發布求職
            else if (post.equals("求职人员")) {
                new Job_II().generateTextJobSeeker(common,false);
            } else if (post.equals("编辑求职")) {
                new Job_II().generateTextJobSeeker(common,true);
            }
        }



        switch (this.text.toLowerCase()) {
            case "/start":
                this.setResponse_job();
                break;

            case "发布招聘":
                if (hasUsername()) {
                    new Job_II().setResponse_jobPosting_management(common);
                } else {
                    this.send_nullUsername();
                }
                break;

            case "发布求职":
                if (hasUsername()) {
                    new Job_II().setResponse_jobSeeker_management(common);
                } else {
                    this.send_nullUsername();
                }
                break;

            case "招聘和求职信息管理":
                new Job_II().setResponse_edit_jobPosting_management(common);
                new Job_II().setResponse_edit_jobSeeker_management(common);
                break;

            // web 端
            // case "发布招聘":
            // if (hasUsername()) {
            // new Job().setResponse_jobPosting_management(common);
            // } else {
            // this.send_nullUsername();
            // }
            // break;
            // case "发布求职":
            // if (hasUsername()) {
            // new Job().setResponse_jobSeeker_management(common);
            // } else {
            // this.send_nullUsername();
            // }
            // break;
            // case "招聘和求职信息管理":
            // new Job().setResponse_edit_jobPosting_management(common);
            // new Job().setResponse_edit_jobSeeker_management(common);
            // break;

            default:
                this.text = "";
                break;
        }
    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.text = this.message.getText();
        this.privateMessageSettings(this.message);
    }

    private void setResponse_job() {
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
        String text = "👋🏻 嗨 " + name + "！\n" +
        // botName + " 能帮您便捷安全地管理群组, Telegram 上最完善的機器人!\n" +
        // "👉🏻 添加我進入超級群組、频道並賦予我管理員以便我能夠操作!\n" +
                "欢迎使用我们的机器人！\n" +
                botName + " 可以帮助您快速找到合适的工作或人才。\n\n" +
                "我们希望这个机器人能为您提供帮助，如果您有任何问题或建议，请随时联系我们。谢谢！";
        // "❓ 指令是什么?\n" +
        // "点击 /help 查看指令以及如何使用它們!";

        this.response.setText(text);
        this.response.setReplyMarkup(new KeyboardButton().jobReplyKeyboardMarkup());
        this.common.sendResponseAsync(this.response);
    }

    // private void setResponse_manage() {
    // this.response.setText(SpringyBotEnum.COMMEND_MANAGE.getText());
    // this.response.setReplyMarkup(new
    // KeyboardButton().manageReplyKeyboardMarkup());
    // this.common.sendResponseAsync(this.response);
    // }

    public void privateMessageSettings(Message message) {
        String chatId = String.valueOf(message.getChatId());
        this.response = new SendMessage();
        this.response.setChatId(chatId);
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }

    private Boolean hasUsername() {
        if (this.common.getUpdate().getMessage().getChat().getUserName() == null) {
            return false;
        }
        return true;
    }

    private void send_nullUsername() {
        this.response.setText("请设置Telegram 用户名称");
        this.common.sendResponseAsync(this.response);
    }

}
