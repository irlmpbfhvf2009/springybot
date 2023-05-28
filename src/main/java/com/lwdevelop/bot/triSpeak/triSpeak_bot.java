package com.lwdevelop.bot.triSpeak;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import com.lwdevelop.bot.triSpeak.handler.CallbackQuerys;
import com.lwdevelop.bot.triSpeak.handler.ChannelMessage;
import com.lwdevelop.bot.triSpeak.handler.GroupMessage;
import com.lwdevelop.bot.triSpeak.handler.PrivateMessage_;
import com.lwdevelop.bot.triSpeak.utils.SpringyBotEnum;
import com.lwdevelop.dto.SpringyBotDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class triSpeak_bot extends TelegramLongPollingBot {

    public Common common;
    private SpringyBotDTO dto;
    private ExecutorService threadPool;

    public triSpeak_bot(SpringyBotDTO springyBotDTO) {
        super(new DefaultBotOptions());
        this.dto = springyBotDTO;
        threadPool = Executors.newFixedThreadPool(10); // 指定執行緒池的大小

        try {
            this.common = new Common(dto.getId(), getMe().getId(), getMe().getUserName());
            this.common.setBot(this);
            this.common.setUserState(new HashMap<>());
            this.common.setConfigDTO_map(new HashMap<>());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {
        this.common.setUpdate(update);
        Message message = update.getMessage();

        // deal message group or private chat
        if (update.hasMessage()) {
            if (message.hasText()) {
                User user = message.getFrom();
                String userInfo = String.format("[%s] @%s (%s %s)", user.getId(), user.getUserName(),
                        user.getFirstName(), user.getLastName());

                // private
                if (message.isUserMessage()) {
                    new PrivateMessage_(this.common).handler();
                    log.info("[{}] Private message received fTErom {}: {}", this.common.getUsername(), userInfo,
                            message.getText());

                }

                // group
                if (message.isSuperGroupMessage()) {
                    threadPool.submit(() -> {
                        new GroupMessage(this.common).handler();
                    });
                    // log.info("[{}] Group message received from {}", common.getUsername(),
                    // userInfo);
                }

            }

            if (message.hasPhoto()) {
            }
        }

        // // deal message channel chat
        if (update.getChannelPost() != null) {
            if (update.getChannelPost().hasText()) {
                String chatType = update.getChannelPost().getChat().getType();
                if (chatTypeIsChannel(chatType)) {
                    new ChannelMessage(this.common).handler();
                }
            }
        }
        // join group event
        try {
            if (common.hasNewChatMembers()) {
                JoinGroup joinGroup = new JoinGroup();
                // is robot join group
                for (User member : message.getNewChatMembers()) {
                    if (common.isBot(member)) {
                        common.dealInviteLink(message.getChatId());
                        joinGroup.isBotJoinGroup(this.common);
                    } else {
                        System.out.println("監聽到用戶入群 update:" + update);
                        joinGroup.isUserJoinGroup(this.common);
                    }
                }
            }

            // leave event
            if (common.hasLeftChatMember()) {
                LeaveGroup leaveGroup = new LeaveGroup(common);
                if (common.isBot_leftChat()) {
                    leaveGroup.isBotLeave();
                }
                if(common.isUser_leftChat()){
                    System.out.println("監聽到用戶退群 update:" + update);
                    leaveGroup.isUserLeave();
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
                    JoinChannel joinChannel = new JoinChannel();
                    // is robot join channel
                    if (common.isBotJoinChannel(chatMemberUpdated)) {
                        common.dealInviteLink(chatMemberUpdated.getChat().getId());
                        joinChannel.isBotJoinChannel(common);
                    }
                    if(common.isUserJoinChannel(chatMemberUpdated)){
                        System.out.println("監聽到用戶入頻道 update:" + update);
                        joinChannel.isUserJoinChannel(common);
                    }
                    // leave event
                    if (common.isBotLeftChannel(chatMemberUpdated)) {
                        new LeaveChannel().isBotLeave(common);
                    }
                    if (common.isUserLeftChannel(chatMemberUpdated)) {
                        System.out.println("監聽到用戶退出頻道 update:" + update);
                        new LeaveChannel().isBotLeave(common);
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.toString());
        }
        if (update.hasCallbackQuery()) {

            new CallbackQuerys(this.common).handler();

            User user = update.getCallbackQuery().getFrom();
            String userInfo = String.format("[%s] %s (%s %s)", user.getId(), user.getUserName(),
                    user.getFirstName(),
                    user.getLastName());
            String data = update.getCallbackQuery().getData();
            log.info("CallbackQuery Data received from {}: {}", userInfo, data);
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

    private Boolean chatTypeIsChannel(String type) {
        return type.equals(SpringyBotEnum.CHAT_TYPE_CHANNEL.getText()) ? true : false;
    }

}
