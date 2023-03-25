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
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Custom extends TelegramLongPollingBot {


    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = 
                    SpringUtils.getApplicationContext().getBean(SpringyBotServiceImpl.class);
            
    private String token;
    private String username;

    // bot details

    private Long botId;
    // private String botUsername;
    // private String botFirstname;
    // private String botLastname;
    // private Boolean canJoinGroups;
    // private Boolean canReadAllGroupMessages;
    // private Boolean supportInlineQueries;


    public Custom(String token, String username) {
        super(new DefaultBotOptions());
        this.token = token;
        this.username = username;
        try {
            this.botId = getMe().getId();
            // this.botFirstname = getMe().getFirstName();
            // this.botUsername = getMe().getUserName();
            // this.botLastname = getMe().getLastName();
            // this.canJoinGroups = getMe().getCanJoinGroups();
            // this.canReadAllGroupMessages = getMe().getCanReadAllGroupMessages();
            // this.supportInlineQueries = getMe().getSupportInlineQueries();

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
        try {
            this.username = getMe().getUserName();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return this.username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        CommonUtils commonUtils = new CommonUtils();
        // deal message if chatType = group or private

        Message message = update.getMessage();

        if (update.hasMessage()) {
            Long chatId = message.getChatId();
            SendMessage response = new SendMessage();
            if (update.getMessage().hasText()) {
                // type : private
                if (update.getMessage().isUserMessage()) {
                    String privateMessage = new PrivateMessage().handler(commonUtils, message, response, this.username);
                        this.sendTextMsg(chatId.toString(),privateMessage,  response);
                }

                // type : group
                if (update.getMessage().isSuperGroupMessage()) {
                    new GroupMessage().handler(commonUtils, message, response);
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
            if (update.getMessage().getNewChatMembers() != null && update.getMessage().getNewChatMembers().size()!=0) {
                // Message message = update.getMessage();
                try {
                    String link = execute(new ExportChatInviteLink(String.valueOf(message.getChat().getId())));
                    new JoinGroupEvent().handler(message,username,this.token,link,springyBotServiceImpl);
                } catch (TelegramApiException e) {
                    String chatId = String.valueOf(message.getFrom().getId());
                    String title = message.getChat().getTitle();
                    sendTextMsg(chatId, title+SpringyBotEnum.BOT_NOT_ENOUGH_RIGHTS.getText());
                    log.error(e.toString());
                }
            }
            
            // 退群或被踢
            if (update.getMessage().getLeftChatMember() != null) {
                new LeaveGroupEvent().handler(message,springyBotServiceImpl);
            }
        } catch (NullPointerException e) {
        }

    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String chatId,String text) {
        SendMessage response = new SendMessage();
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String chatId,String text, SendMessage response) {
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

}