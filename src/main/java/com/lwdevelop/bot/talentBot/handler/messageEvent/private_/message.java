package com.lwdevelop.bot.talentBot.handler.messageEvent.private_;

import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.service.impl.RobotGroupAndChannelManagementServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.talentBot.handler.messageEvent.private_.commands.Job;
import com.lwdevelop.bot.talentBot.utils.Common;
import com.lwdevelop.bot.talentBot.utils.KeyboardButton;
import com.lwdevelop.bot.talentBot.utils.SpringyBotEnum;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;

public class message {

    private Common common;
    private Message message;
    private String text;
    private SendMessage response;
    private Job job_II;

    @Autowired
    private RobotGroupAndChannelManagementServiceImpl robotGroupAndChannelManagementService = SpringUtils.getApplicationContext()
            .getBean(RobotGroupAndChannelManagementServiceImpl.class);

    public void handler(Common common) {
        this.init(common);
        boolean ifSubscribeChannel = robotGroupAndChannelManagementService.ifSubscribeChannel(common);
        // 判斷是否有關注頻道
        if (ifSubscribeChannel){
            // 判斷事件
            if (text.length() >= 4) {
                String post = text.substring(0, 4);
                    // 發布招聘
                    if (post.equals(SpringyBotEnum.RECRUITMENT.getText())) {
                        this.job_II.generateTextJobPosting(common, false);
                    } else if (post.equals(SpringyBotEnum.EDIT_RECRUITMENT.getText())) {
                        this.job_II.generateTextJobPosting(common, true);
                    }

                    // 發布求職
                    else if (post.equals(SpringyBotEnum.JOBSEARCH.getText())) {
                        this.job_II.generateTextJobSeeker(common, false);
                    } else if (post.equals(SpringyBotEnum.EDIT_JOBSEARCH.getText())) {
                        this.job_II.generateTextJobSeeker(common, true);
                    }
            }

                switch (this.text.toLowerCase()) {
                    case "/start":
                        this.setResponse_job();
                        break;

                    case "发布招聘":
                        if (hasUsername()) {
                            this.job_II.setResponse_jobPosting_management(common);
                        } else {
                            this.send_nullUsername();
                        }
                        break;

                    case "发布求职":
                        if (hasUsername()) {
                            this.job_II.setResponse_jobSeeker_management(common);
                        } else {
                            this.send_nullUsername();
                        }
                        break;

                    case "招聘和求职信息管理":
                            this.job_II.setResponse_edit_jobPosting_management(common);
                            this.job_II.setResponse_edit_jobSeeker_management(common);
                        break;

                    default:
                        this.text = "";
                        break;
                  }
    }else {
            SendMessage s = new SendMessage();
            s.setChatId(common.getUpdate().getMessage().getChatId().toString());
            s.setText("✅ 官方频道\n" +
                    "➡️ @rc499 ️\n" +
                    "\uD83D\uDD08关注后可发布");
            common.sendResponseAsync(s);
        }
    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.text = this.message.getText();
        this.privateMessageSettings(this.message);
        this.job_II = new Job(new JobPostingDTO(common), new JobSeekerDTO(common));
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
        this.response.setText(SpringyBotEnum.PLEASE_SET_TELEGRAM_USERNAME.getText());
        this.common.sendResponseAsync(this.response);
    }

}
