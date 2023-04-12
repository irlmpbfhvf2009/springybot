package com.lwdevelop.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.handler.CallbackQuerys;
import com.lwdevelop.bot.handler.event.JoinGroup;
import com.lwdevelop.bot.handler.event.LeaveGroup;
import com.lwdevelop.bot.handler.messageEvent.private_.*;
import com.lwdevelop.bot.handler.messageEvent.channel.ChannelMessage;
import com.lwdevelop.bot.handler.messageEvent.group.GroupMessage;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.SpringyBotEnum;
import com.lwdevelop.dto.SpringyBotDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Custom extends TelegramLongPollingBot {

    public Common common;
    private SpringyBotDTO dto;
    private Message message;
    private SendMessage response;

    public Custom(SpringyBotDTO springyBotDTO) {
        super(new DefaultBotOptions());
        this.dto = springyBotDTO;

        try {
            this.common = new Common(dto.getId(), getMe().getId(), getMe().getUserName());
            this.common.setBot(this);
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

    // @Override
    // public void onChatMemberUpdateReceived(ChatMemberUpdated chatMemberUpdated) {
    // // handle chat member update event
    // ChatMember newChatMember = chatMemberUpdated.getNewChatMember();
    // myService.processChatMemberUpdate(newChatMember);
    // }

    @Override
    public void onUpdateReceived(Update update) {

        this.init(update);

        // deal message group or private chat
        if (update.hasMessage()) {
            if (this.message.hasText()) {

                // private
                if (this.message.isUserMessage()) {
                    new message().handler(this.common);
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
                        new JoinGroup().isBotJoinGroup(this.common);
                    } else {
                        new JoinGroup().isUserJoinGroup(this.common);
                    }
                }
            }

            // leave event
            if (isLeftChatMemberNotNull()) {
                if (isBot_leftChat()) {
                    new LeaveGroup().isBotLeave(common);

                }
            }
        } catch (Exception e) {
        }

        // join channel event
        try {
            ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
            if (chatMemberUpdated != null) {
                if (chatTypeIsChannel(chatMemberUpdated.getChat().getType())) {
                    System.out.println("chatMemberUpdated=" + chatMemberUpdated);
                    System.out.println("chatMemberUpdated.getFrom()="+chatMemberUpdated.getFrom());
                    System.out.println("chatMemberUpdated.getInviteLink()="+chatMemberUpdated.getInviteLink());
                    System.out.println("getUser = " + chatMemberUpdated.getNewChatMember().getUser());

                }
            }

        } catch (Exception e) {
        }

        if (update.hasCallbackQuery()) {
            new CallbackQuerys().handler(common);
        }

    }

    private void init(Update update) {
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
            this.notEnoughRightsMessageSettings(this.message);
            this.common.setInviteLink("");
            this.common.sendResponseAsync(this.response);
            log.error(e.toString());
        }
    }

    public void notEnoughRightsMessageSettings(Message message) {
        String chatId = String.valueOf(message.getFrom().getId());
        String title = message.getChat().getTitle();
        this.response = new SendMessage();
        this.response.setChatId(chatId);
        this.response.setDisableNotification(false);
        this.response.setText(title + SpringyBotEnum.BOT_NOT_ENOUGH_RIGHTS.getText());
    }

}