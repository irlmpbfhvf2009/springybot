package com.lwdevelop.bot.talent;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.Common;
import com.lwdevelop.bot.event.JoinChannel;
import com.lwdevelop.bot.event.JoinGroup;
import com.lwdevelop.bot.event.LeaveChannel;
import com.lwdevelop.bot.event.LeaveGroup;
import com.lwdevelop.bot.talent.handler.CallbackQuerys;
import com.lwdevelop.bot.talent.handler.ChannelMessage;
import com.lwdevelop.bot.talent.handler.GroupMessage;
import com.lwdevelop.bot.talent.handler.PrivateMessage_;
import com.lwdevelop.bot.talent.handler.commands.Job;
import com.lwdevelop.dto.SpringyBotDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class talent_bot extends TelegramLongPollingBot {

    private Common common;
    private SpringyBotDTO dto;

    public talent_bot(SpringyBotDTO springyBotDTO) {
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

    @Override
    public void onUpdateReceived(Update update) {

        this.common.setUpdate(update);
        Message message = update.getMessage();

        // deal message group or private chat
        if (update.hasMessage()) {
            if (message.hasText()) {
                User user = message.getFrom();
                String username = user.getUserName() == null ? "" : user.getUserName();
                String userInfo = String.format("[%s] %s", user.getId(), username);

                // private
                if (message.isUserMessage()) {
                    new Job().saveJobUser(common);
                    new PrivateMessage_(this.common).handler();
                    log.info("[{}] Private message received from {}: {}", common.getUsername(), userInfo,
                            message.getText());

                }

                // group
                if (message.isSuperGroupMessage()) {
                    new GroupMessage(this.common).handler();
                    // log.info("[{}] Group message received from {}", common.getUsername(),userInfo);
                }
            }
        }

        // // deal message channel chat
        if (update.getChannelPost() != null) {
            if (update.getChannelPost().hasText()) {
                String chatType = update.getChannelPost().getChat().getType();
                if (common.chatTypeIsChannel(chatType)) {
                    new ChannelMessage(this.common).handler();
                    log.info("[{}] Channel message received from {}",common.getUsername(), update.getChannelPost().getAuthorSignature());
                }
            }
        }

        // join group event
        try {
            if (common.hasNewChatMembers()) {
                JoinGroup joinGroup = new JoinGroup(this.common);
                // is robot join group
                for (User member : message.getNewChatMembers()) {
                    if (common.isBot(member)) {
                        common.dealInviteLink(message.getChatId());
                        joinGroup.isBotJoinGroup();
                    } else {
                        joinGroup.isUserJoinGroup();
                    }
                }
            }

            // leave event
            if (common.hasLeftChatMember()) {
                LeaveGroup leaveGroup = new LeaveGroup(this.common);
                if (common.isBot_leftChat()) {
                    leaveGroup.isBotLeave();
                }
            }
        } catch (Exception e) {
        }

        // join channel event
        try {
            if (update.getMyChatMember() != null) {

                ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
                common.setChatMemberUpdated(chatMemberUpdated);

                if (common.chatTypeIsChannel(chatMemberUpdated.getChat().getType())) {

                    // is robot join channel
                    if (common.isBotJoinChannel(chatMemberUpdated)) {
                        common.dealInviteLink(chatMemberUpdated.getChat().getId());
                        new JoinChannel(common).isBotJoinChannel();
                    }

                    // leave event
                    LeaveChannel leaveChannel= new LeaveChannel(common);
                    if (common.isBotLeftChannel(chatMemberUpdated)) {
                        leaveChannel.isBotLeaveChannel();
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.toString());
        }

        if (update.hasCallbackQuery()) {
            new CallbackQuerys(this.common).handler();
            User user = update.getCallbackQuery().getFrom();
            String username = user.getUserName() == null ? "無題" : user.getUserName();
            String userInfo = String.format("[%s] %s", user.getId(), username);
            String data = update.getCallbackQuery().getData();
            log.info("CallbackQuery Data received from {}: {}", userInfo, data);
        }

    }
    // private Boolean hasNewChatMembers() {
    //     return this.message.getNewChatMembers() != null && this.message.getNewChatMembers().size() != 0;
    // }

    // private Boolean isBot(User member) {
    //     return member.getUserName().equals(this.common.getUsername()) && member.getIsBot();
    // }

    // private Boolean isBot_leftChat() {
    //     return this.message.getLeftChatMember().getIsBot()
    //             && this.message.getLeftChatMember().getUserName().equals(this.common.getUsername());
    // }

    // private Boolean hasLeftChatMember() {
    //     return this.message.getLeftChatMember() != null;
    // }

    // private Boolean chatTypeIsChannel(String type) {
    //     return type.equals(SpringyBotEnum.CHAT_TYPE_CHANNEL.getText()) ? true : false;
    // }

    // private Boolean isBotJoinChannel(ChatMemberUpdated chatMemberUpdated) {
    //     return chatMemberUpdated.getNewChatMember().getUser().getIsBot()
    //             && chatMemberUpdated.getNewChatMember().getStatus().equals("administrator");
    // }

    // private Boolean isBotLeftChannel(ChatMemberUpdated chatMemberUpdated) {
    //     return chatMemberUpdated.getNewChatMember().getUser().getIsBot()
    //             && chatMemberUpdated.getNewChatMember().getStatus().equals("left");
    // }


    // private void dealInviteLink(Long chatId) {
    //     try {
    //         String inviteLink = execute(
    //                 new ExportChatInviteLink(String.valueOf(chatId)));
    //         this.common.setInviteLink(inviteLink);
    //     } catch (TelegramApiException e) {
    //         String title = this.message.getChat().getTitle();
    //         this.response = new SendMessage();
    //         this.response.setChatId(String.valueOf(message.getFrom().getId()));
    //         this.response.setText(title + SpringyBotEnum.BOT_NOT_ENOUGH_RIGHTS.getText());
    //         this.common.setInviteLink("");
    //         this.common.executeAsync(this.response);
    //         log.error(e.toString());
    //     }
    // }
}