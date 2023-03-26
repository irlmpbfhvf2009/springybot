package com.lwdevelop.bot;

import java.util.List;

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
import com.lwdevelop.bot.handler.PrivateMessage;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.dto.SpringyBotDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Custom extends TelegramLongPollingBot {

    private Common common;
    private SpringyBotDTO dto;
    private Message message;
    private Update update;

    public Custom(SpringyBotDTO springyBotDTO) {
        super(new DefaultBotOptions());
        this.dto = springyBotDTO;

        try {
            this.common = new Common(dto.getId(), getMe().getId(), getMe().getUserName());
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

        this.common.setMessage(update.getMessage());
        this.message = update.getMessage();
        this.update = update;

        // deal message group or private chat
        if (isHasMessage()) {
            if (hasMessage()) {

                // private
                if (isUserMessage()) {
                    privateMessageHandler();
                }

                // group
                if (isSuperGroupMessage()) {
                    groupMessageHandler();
                }
            }
        }

        // // deal message channel chat
        if (getChannelPostNotNull()) {
            if (isChannelPostHasText()) {
                if (chatTypeIsChannel()) {
                    channelMessageHandler();
                }
            }
        }

        // join group event
        try {
            if (isNewChatMembersNotNullAndIsNewChatMembersNotEmpty()) {
                // is robot join group
                for (User member : getNewChatMembers()) {
                    if (isBot(member)) {
                        joinGroupEventIsBotJoinGroup();
                    } else {
                        joinGroupEventIsUserJoinGroup();
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
    public void sendTextMsg() {
        executeAsync(this.common.getResponse());
    }

    private Boolean chatTypeIsChannel() {
        return this.common.chatTypeIsChannel(getChannelPostChatTyoe());
    }

    private Boolean getChannelPostNotNull() {
        return this.update.getChannelPost() != null;
    }

    private Boolean hasMessage() {
        return this.message.hasText();
    }

    private Boolean isHasMessage() {
        return this.update.hasMessage();
    }

    private Boolean isChannelPostHasText() {
        return this.update.getChannelPost().hasText();
    }

    private Boolean isUserMessage() {
        return this.message.isUserMessage();
    }

    private Boolean isSuperGroupMessage() {
        return this.message.isSuperGroupMessage();
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

    private String getChannelPostChatTyoe() {
        return this.update.getChannelPost().getChat().getType();
    }
    
    private List<User> getNewChatMembers() {
        return this.message.getNewChatMembers();
    }

    private void channelMessageHandler() {
        new ChannelMessage().handler(this.common);
    }

    private void privateMessageHandler() {
        new PrivateMessage().handler(this.common);
        this.sendTextMsg();
    }

    private void groupMessageHandler() {
        new GroupMessage().handler(this.common);
    }


    private void joinGroupEventIsUserJoinGroup(){
        new JoinGroupEvent().isUserJoinGroup(this.common);
    }

    private void joinGroupEventIsBotJoinGroup() {
        dealInviteLink();
        new JoinGroupEvent().isBotJoinGroup(this.common);
    }


    private void dealInviteLink() {
        try {
            String inviteLink = execute(new ExportChatInviteLink(String.valueOf(this.message.getChatId())));
            this.common.setInviteLink(inviteLink);
        } catch (TelegramApiException e) {
            this.common.notEnoughRightsMessageSettings(this.message);
            this.common.setInviteLink("");
            sendTextMsg();
            log.error(e.toString());
        }
    }
}