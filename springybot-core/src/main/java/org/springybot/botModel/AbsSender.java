package org.springybot.botModel;

import java.util.concurrent.ExecutionException;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class AbsSender {

    protected DefaultAbsSender bot;

    @Async
    // @SneakyThrows
    public Integer executeAsync(SendMessage sendMessage) {
        try {
            return this.bot.executeAsync(sendMessage).get().getMessageId();
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
    public void executeAsync(SendChatAction sendChatAction) {
        this.bot.executeAsync(sendChatAction);
    }

    @Async
    @SneakyThrows
    public void executeAsync(DeleteMessage deleteMessage) {
        this.bot.executeAsync(deleteMessage);
    }

    @Async
    public Boolean executeAsync(GetChatMember getChatMember) {
        String status;
        try {
            status = this.bot.executeAsync(getChatMember).get().getStatus();
            if (status.equals("left") || status.equals("kicked")) {
                return false;
            } else {
                return true;
            }
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
    public ChatMember getChatMember(GetChatMember getChatMember) {
        return this.bot.executeAsync(getChatMember).get();
    }

    @Async
    @SneakyThrows
    public Chat executeAsync(GetChat getChat) {
        return this.bot.executeAsync(getChat).get();
    }

    @Async
    @SneakyThrows
    public void executeAsync(RestrictChatMember restrictChatMember) {
        this.bot.executeAsync(restrictChatMember);
    }
}
