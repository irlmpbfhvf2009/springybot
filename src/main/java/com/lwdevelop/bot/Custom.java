package com.lwdevelop.bot;

import java.util.HashMap;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.handler.ChannelMessage;
import com.lwdevelop.bot.handler.GroupMessage;
import com.lwdevelop.bot.handler.JoinGroupEvent;
import com.lwdevelop.bot.handler.LeaveGroupEvent;
import com.lwdevelop.bot.handler.Commends;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.SpringyBotEnum;
import com.lwdevelop.dto.SpringyBotDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Custom extends TelegramLongPollingBot {

    public Common common;
    private SpringyBotDTO dto;
    private Message message;

    public Custom(SpringyBotDTO springyBotDTO) {
        super(new DefaultBotOptions());
        this.dto = springyBotDTO;

        try {
            this.common = new Common(dto.getId(), getMe().getId(), getMe().getUserName());
            this.common.setUserState(new HashMap<>());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotToken() {
        return this.dto.getToken();
    }

    @Override
    public String getBotUsername() {
        return this.dto.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {

        this.init(update);

        // deal message group or private chat
        if (update.hasMessage()) {
            if (this.message.hasText()) {

                // private
                if (this.message.isUserMessage()) {
                    new Commends().handler(this.common);
                    sendMsg();
                }

                // group
                if (this.message.isSuperGroupMessage()) {
                    new GroupMessage().handler(this.common);
                }
            }
        }

        // // deal message channel chat
        if (update.getChannelPost() != null) {
            if (update.getChannelPost().hasText()) {
                String chatType = update.getChannelPost().getChat().getType();
                if (chatTypeIsChannel(chatType)) {
                    new ChannelMessage().handler(this.common);
                }
            }
        }

        // join group event
        try {
            if (isNewChatMembersNotNullAndIsNewChatMembersNotEmpty()) {
                // is robot join group
                for (User member : this.message.getNewChatMembers()) {
                    if (isBot(member)) {
                        dealInviteLink();
                        new JoinGroupEvent().isBotJoinGroup(this.common);
                    } else {
                        new JoinGroupEvent().isUserJoinGroup(this.common);
                    }
                }
            }

            // leave event
            if (isLeftChatMemberNotNull()) {
                if (isBot_leftChat()) {
                    new LeaveGroupEvent().isBotLeave(common);

                }
            }
        } catch (NullPointerException e) {
        }

    }

    @SneakyThrows
    @Async
    public void sendMsg() {
        executeAsync(this.common.getResponse());
    }
    private void init(Update update){
        this.common.setUpdate(update);
        this.message = update.getMessage();
    }
    private Boolean isNewChatMembersNotNullAndIsNewChatMembersNotEmpty() {
        return this.message.getNewChatMembers() != null && this.message.getNewChatMembers().size() != 0;
    }

    private Boolean isBot(User member) {
        return member.getUserName().equals(this.common.getUsername()) && member.getIsBot();
    }

    private Boolean isBot_leftChat() {
        return this.message.getLeftChatMember().getIsBot()
                && this.message.getLeftChatMember().getUserName().equals(this.common.getUsername());
    }

    private Boolean isLeftChatMemberNotNull() {
        return this.message.getLeftChatMember() != null;
    }

    public boolean chatTypeIsChannel(String type) {
        return type.equals(SpringyBotEnum.CHAT_TYPE_CHANNEL.getText()) ? true : false;
    }
    
    private void dealInviteLink() {
        try {
            String inviteLink = execute(new ExportChatInviteLink(String.valueOf(this.message.getChatId())));
            this.common.setInviteLink(inviteLink);
        } catch (TelegramApiException e) {
            this.common.notEnoughRightsMessageSettings(this.message);
            this.common.setInviteLink("");
            sendMsg();
            log.error(e.toString());
        }
    }
}