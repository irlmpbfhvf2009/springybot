package com.lwdevelop.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.talent.utils.SpringyBotEnum;
import com.lwdevelop.dto.ConfigDTO;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Common {

    private Long springyBotId;

    private Long botId;

    private String username;

    private Update update;

    private String inviteLink;

    private TelegramLongPollingBot bot;

    // 用来存储用户的状态(会话)
    private HashMap<Long, String> userState;

    private HashMap<Long, ConfigDTO> configDTO_map;

    private HashMap<Long, List<String>> groupMessageMap;

    public Common(Long springyBotId, Long botId, String username) {
        this.springyBotId = springyBotId;
        this.botId = botId;
        this.username = username;
    }

    @Async
    public void deleteMessageTask(List<DeleteMessage> deleteSystemMessages, int second) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (DeleteMessage deleteMessage : deleteSystemMessages) {
                    executeAsync(deleteMessage);
                }
            }
        }, second * 1000);
    }

    @Async
    public void deleteMessageTask(DeleteMessage deleteSystemMessage, int second) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                executeAsync(deleteSystemMessage);
            }
        }, second * 1000);
    }

    @Async
    @SneakyThrows
    public Integer executeAsync(SendMessage sendMessage) {
        return this.bot.executeAsync(sendMessage).get().getMessageId();
    }

    @Async
    @SneakyThrows
    public void executeAsync_(SendMessage sendMessage) {
        this.bot.execute(sendMessage);
    }

    @Async
    @SneakyThrows
    public Integer executeAsync(SendPhoto sendPhoto) {
        return this.bot.executeAsync(sendPhoto).get().getMessageId();
    }

    @Async
    @SneakyThrows
    public void executeAsync(EditMessageText editMessageText) {
        this.bot.executeAsync(editMessageText);
    }

    @Async
    @SneakyThrows
    public void executeAsync(DeleteMessage deleteMessage) {
        this.bot.executeAsync(deleteMessage);
    }

    @Async
    public String executeAsync(GetChatMember getChatMember) {
        try {
            return this.bot.executeAsync(getChatMember).get().getStatus();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Async
    @SneakyThrows
    public Chat executeAsync(GetChat getChat) {
        return this.bot.executeAsync(getChat).get();
    }

    @Async
    // @SneakyThrows
    public void executeAsync(RestrictChatMember restrictChatMember) {
        try {
            this.bot.executeAsync(restrictChatMember);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String formatBot(){
        return "["+this.botId+"] "+this.username;
    }

    public String formatUser(User user) {
        String firstname = user.getFirstName() == null ? "" : user.getFirstName();
        String username = user.getUserName() == null ? "" : user.getUserName();
        String lastname = user.getLastName() == null ? "" : user.getLastName();
        String name = username == null ? firstname + lastname : username;
        return "[" + user.getId() + "] " + name;
    }

    public void dealInviteLink(Long chatId) {
        try {
            String inviteLink = this.bot.execute(
                    new ExportChatInviteLink(String.valueOf(chatId)));
            setInviteLink(inviteLink);
        } catch (TelegramApiException e) {
            Message message = this.update.getMessage();
            String title = message.getChat().getTitle();
            SendMessage response = new SendMessage();
            response.setChatId(String.valueOf(message.getFrom().getId()));
            response.setText(title + SpringyBotEnum.BOT_NOT_ENOUGH_RIGHTS.getText());
            setInviteLink("");
            executeAsync(response);
            log.error(e.toString());
        }
    }
}
