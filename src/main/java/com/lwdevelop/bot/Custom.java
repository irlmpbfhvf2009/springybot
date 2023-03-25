package com.lwdevelop.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.handler.ChannelMessage;
import com.lwdevelop.bot.handler.GroupMessage;
import com.lwdevelop.bot.handler.JoinGroupEvent;
import com.lwdevelop.bot.handler.LeaveGroupEvent;
import com.lwdevelop.bot.handler.PrivateMessage;
import com.lwdevelop.bot.utils.CommonUtils;
import com.lwdevelop.bot.utils.SpringyBotEnum;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.RobotGroupManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Custom extends TelegramLongPollingBot {

    @Autowired
    private RobotGroupManagementServiceImpl robotGroupManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(RobotGroupManagementServiceImpl.class);

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private Long id;
    private Long botId;
    private String token;
    private String username;


    public Custom(SpringyBotDTO springyBotDTO) {
        super(new DefaultBotOptions());

        SpringyBot springyBot = springyBotServiceImpl.findById(springyBotDTO.getId()).get();
        springyBot.setState(true);
        springyBotServiceImpl.save(springyBot);
        this.id = springyBotDTO.getId();
        this.token = springyBot.getToken();
        try {
            this.username = getMe().getUserName();
            this.botId = getMe().getId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        CommonUtils commonUtils = new CommonUtils();
        Message message = update.getMessage();
        SpringyBot springyBot = springyBotServiceImpl.findById(this.id).get();

        // deal message if chatType = group or private
        if (update.hasMessage()) {
            Long chatId = message.getChatId();
            SendMessage response = new SendMessage();
            if (update.getMessage().hasText()) {
                // type : private
                if (update.getMessage().isUserMessage()) {
                    String privateMessage = new PrivateMessage().handler(commonUtils, message, response, this.username);
                    this.sendTextMsg(chatId.toString(), privateMessage, response);

                }

                // type : group
                if (update.getMessage().isSuperGroupMessage()) {
                    new GroupMessage().handler(commonUtils, message, response, springyBot);
                }
            }
        }

        // deal message if chatType = channel
        if (update.getChannelPost() != null) {
            // type : channel
            String chatType = update.getChannelPost().getChat().getType();
            if (update.getChannelPost().hasText()) {
                if (commonUtils.chatTypeIsChannel(chatType)) {
                    new ChannelMessage().handler();
                }
            }
        }

        // 群組新成員
        try {
            if (update.getMessage().getNewChatMembers() != null
                    && update.getMessage().getNewChatMembers().size() != 0) {
                String link = "";
                try {
                    link = execute(new ExportChatInviteLink(String.valueOf(message.getChat().getId())));
                } catch (TelegramApiException e) {
                    String chatId = String.valueOf(message.getFrom().getId());
                    String title = message.getChat().getTitle();
                    sendTextMsg(chatId, title + SpringyBotEnum.BOT_NOT_ENOUGH_RIGHTS.getText());
                    log.error(e.toString());
                } finally {
                    new JoinGroupEvent().handler(message, this.username, this.botId,springyBot, link,springyBotServiceImpl);
                }
            }

            // 退群或被踢
            if (update.getMessage().getLeftChatMember() != null) {
                new LeaveGroupEvent().handler(message, this.botId, springyBot, robotGroupManagementServiceImpl,
                        springyBotServiceImpl);
            }
        } catch (NullPointerException e) {
        }

    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String chatId, String text) {
        SendMessage response = new SendMessage();
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String chatId, String text, SendMessage response) {
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

}