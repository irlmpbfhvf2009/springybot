package com.lwdevelop.bot;

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
import com.lwdevelop.bot.utils.CommonUtils;
import com.lwdevelop.dto.SpringyBotDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Custom extends TelegramLongPollingBot {

    private CommonUtils common;
    private SpringyBotDTO dto;
    private Message message;

    public Custom(SpringyBotDTO springyBotDTO) {
        super(new DefaultBotOptions());
        this.dto = springyBotDTO;
        
        try {
            this.common = new CommonUtils(dto.getId(),getMe().getId(),getMe().getUserName());
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

        // deal message group or private chat
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {

                // private
                if (isUserMessage()) {
                    new PrivateMessage().handler(common);
                    this.sendTextMsg();
                }

                // group
                if (isSuperGroupMessage()) {
                    new GroupMessage().handler(common);
                }
            }
        }

        // // deal message channel chat 
        if (update.getChannelPost() != null) {
            if (update.getChannelPost().hasText()) {
                String chatType = update.getChannelPost().getChat().getType();
                if (common.chatTypeIsChannel(chatType)) {
                    new ChannelMessage().handler(common);
                }
            }
        }

        // join group event
        try {
            if (isNewChatMembersNotNull() && isNewChatMembersNotEmpty()) {
                // is robot join group
                for (User member : this.message.getNewChatMembers()) {
                    if (isBot(member)) {
                        dealInviteLink();
                        new JoinGroupEvent().isBotJoinGroup(common);
                    }else{
                        new JoinGroupEvent().isUserJoinGroup(common);
                    }
                }
            }

            // leave event
            if (isLeftChatMemberNotNull()) {
                if(isBot_leftChat()){
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

    private Boolean isUserMessage(){
        return this.message.isUserMessage();
    }

    private Boolean isSuperGroupMessage(){
        return this.message.isSuperGroupMessage();
    }

    private Boolean isNewChatMembersNotNull(){
        return this.message.getNewChatMembers() != null;
    }

    private Boolean isNewChatMembersNotEmpty(){
        return this.message.getNewChatMembers().size() != 0;
    }

    private Boolean isBot(User member){
        return member.getUserName().equals(this.common.getUsername()) && member.getIsBot();
    }

    private Boolean isBot_leftChat(){
        return this.message.getLeftChatMember().getIsBot() && this.message.getLeftChatMember().getUserName().equals(this.common.getUsername());
    }

    private Boolean isLeftChatMemberNotNull(){
        return this.message.getLeftChatMember() != null;
    }

    private void dealInviteLink(){
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