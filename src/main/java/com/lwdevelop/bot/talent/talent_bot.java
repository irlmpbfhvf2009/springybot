package com.lwdevelop.bot.talent;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
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
                    // log.info("[{}] Group message received from {}",
                    // common.getUsername(),userInfo);
                }
            }
        }

        // // deal message channel chat
        if (update.getChannelPost() != null) {
            if (update.getChannelPost().hasText()) {
                String chatType = update.getChannelPost().getChat().getType();
                if (common.chatTypeIsChannel(chatType)) {
                    new ChannelMessage(this.common).handler();
                    log.info("[{}] Channel message received from {}", common.getUsername(),
                            update.getChannelPost().getAuthorSignature());
                }
            }
        }

        // join or leave event
        if (update.hasMyChatMember()) {
            // Type of chat, can be either private, group, supergroup or channel .
            String type = update.getMyChatMember().getChat().getType();
            ChatMember chatMember = update.getMyChatMember().getNewChatMember();
            Boolean isBot = chatMember.getUser().getIsBot();
            // The member’s status in the chat. Can be ADMINISTRATOR, OWNER, BANNED, LEFT,
            // MEMBER or RESTRICTED.
            Boolean isJoin = chatMember.getStatus().equals("left") ? false : true;

            // is robot join or leave group
            if (type.equals("group") || type.equals("supergroup")) {
                if (isJoin) {
                    JoinGroup joinGroup = new JoinGroup(this.common);
                    joinGroup.handler(isBot);
                } else {
                    LeaveGroup leaveGroup = new LeaveGroup(this.common);
                    leaveGroup.handler(isBot);
                }
            }
            // is robot join or leave channel
            if (type.equals("channel")) {
                if (isJoin) {
                    JoinChannel joinChannel = new JoinChannel(common);
                    joinChannel.handler(isBot);

                } else {
                    LeaveChannel leaveChannel = new LeaveChannel(common);
                    leaveChannel.handler(isBot);
                }

            }
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
}