package com.lwdevelop.bot.factory;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.utils.RedisUtils;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseLongPollingBot extends CustomLongPollingBot {

    @Autowired
    private RedisUtils redisUtils = SpringUtils.getApplicationContext()
            .getBean(RedisUtils.class);

    protected Common common;
    protected Update update;
    protected Message message;
    protected String type;
    protected ChatMember chatMember;

    public BaseLongPollingBot(SpringyBotDTO dto) {
        super(dto);
        this.common = initializeCommon(dto);
        try {
            GetUpdates getUpdates = new GetUpdates();
            getUpdates.setOffset(0);
            this.execute(getUpdates);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Common initializeCommon(SpringyBotDTO dto) {
        try {
            Common common = new Common();
            common.setUpdate(update);
            common.setSpringyBotId(dto.getId());
            common.setBotId(getMe().getId());
            common.setBotUsername(getMe().getUserName());
            common.setBot(this);
            common.setUserState(new HashMap<>());
            common.setGroupMessageMap(new HashMap<>());
            common.setGroupMessageMap2(new HashMap<>());
            return common;
        } catch (TelegramApiException e) {
            log.error("Failed to initialize Common", e);
            throw new RuntimeException("Failed to initialize Common", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.common.setUpdate(update);
        this.update = update;
        this.message = update.getMessage();

        if (update.hasMessage()) {
            if (message.hasText()) {
                if (message.isUserMessage()) {
                    handlePrivateMessage();
                    User user = message.getFrom();
                    String userInfo = String.format("[%s] @%s (%s %s)", user.getId(), user.getUserName(),
                            user.getFirstName(), user.getLastName());
                    log.info("[{}] Private message received from {}: {}", this.common.getBotUsername(), userInfo,
                            this.message.getText());
                }
                if (message.isSuperGroupMessage() || message.isGroupMessage()) {

                    // 監聽群組消息,檢查數據庫是否記錄到該群組,無則紀錄
                    this.checkAndRecordGroupInDatabase();

                    handleGroupMessage();
                }
            }

            if (message.hasPhoto()) {
                handlePhotoMessage();
            }
        }

        if (update.hasCallbackQuery()) {
            User user = update.getCallbackQuery().getFrom();
            String userInfo = String.format("[%s] %s (%s %s)", user.getId(), user.getUserName(), user.getFirstName(),
                    user.getLastName());
            handleCallbackQuery();
            log.info("CallbackQuery Data received from {}: {}", userInfo, update.getCallbackQuery().getData());
        }

        if (update.hasChannelPost()) {
            if (update.getChannelPost().getChat().getType().equals("channel")) {
                // 監聽群組消息,檢查數據庫是否記錄到該群組,無則紀錄
                this.checkAndRecordChannelInDatabase();
                handleChannelPost();
            }

        }

        if (update.hasMyChatMember() || update.hasChatMember()) {
            if (update.hasMyChatMember()) {
                this.type = update.getMyChatMember().getChat().getType();
                this.chatMember = update.getMyChatMember().getNewChatMember();
            }
            if (update.hasChatMember()) {
                this.type = update.getChatMember().getChat().getType();
                this.chatMember = update.getChatMember().getNewChatMember();
            }

            if (this.chatMember != null && this.type != null) {
                handleChatMemberUpdate();
            }

        }
    }

    protected abstract void handlePrivateMessage();

    protected abstract void handleGroupMessage();

    protected abstract void handlePhotoMessage();

    protected abstract void handleCallbackQuery();

    protected abstract void handleChannelPost();

    protected abstract void handleChatMemberUpdate();

    private void checkAndRecordGroupInDatabase() {
        // 監聽群组消息，檢查Redis數據庫是否紀錄到該群組，無則紀錄
        List<RobotGroupManagement> robotGroupManagements = redisUtils.get("RobotGroupManagement_" + common.getSpringyBotId(),
                new TypeReference<List<RobotGroupManagement>>() {
                });
        robotGroupManagements.stream()
                .filter(rgm -> rgm.getGroupId().equals(message.getChatId()))
                .findAny()
                .ifPresentOrElse(rgm -> {
                    rgm.setType(message.getChat().getType());
                }, () -> {
                    RobotGroupManagement robotGroupManagement = new RobotGroupManagement();
                    robotGroupManagement.setBotId(common.getBotId());
                    robotGroupManagement.setGroupId(message.getChatId());
                    robotGroupManagement.setGroupTitle(message.getChat().getTitle());
                    robotGroupManagement.setStatus(true);
                    robotGroupManagement.setType(message.getChat().getType());
                    robotGroupManagements.add(robotGroupManagement);
                });
        redisUtils.set("RobotGroupManagement_" + common.getSpringyBotId(), robotGroupManagements);
    }

    private void checkAndRecordChannelInDatabase() {
        // 監聽頻道消息，檢查Redis數據庫是否紀錄到該頻道，無則紀錄
        List<RobotChannelManagement> robotChannelManagements = redisUtils.get("RobotChannelManagement_" + common.getSpringyBotId(),
                new TypeReference<List<RobotChannelManagement>>() {
                });
        robotChannelManagements.stream()
                .filter(rcm -> rcm.getChannelId().equals(update.getChannelPost().getChat().getId()))
                .findAny()
                .ifPresentOrElse(rcm -> {
                }, () -> {
                    RobotChannelManagement robotChannelManagement = new RobotChannelManagement();
                    robotChannelManagement.setBotId(common.getBotId());
                    robotChannelManagement.setChannelId(update.getChannelPost().getChat().getId());
                    robotChannelManagement.setChannelTitle(message.getChat().getTitle());
                    robotChannelManagement.setStatus(true);
                    robotChannelManagements.add(robotChannelManagement);
                });
        redisUtils.set("RobotChannelManagement_" + common.getSpringyBotId(), robotChannelManagements);
    }
}
