package com.lwdevelop.bot;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.SneakyThrows;

public class Custom extends TelegramLongPollingBot {

    private String token;
    private String username;

    public Custom(String token, String username, DefaultBotOptions options) {
        super(options);
        this.token = token;
        this.username = username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        System.out.println(update.getMessage().getNewChatMembers());

        if (update.hasMessage()) {
            Message message = update.getMessage();
            System.out.println(update);
            Long chatId = message.getChatId();
            String text = message.getText();
            SendMessage response = new SendMessage();
            if (update.getMessage().hasText()) {
                // System.out.println(message);
                // System.out.println(message.getChat().getType());
                if (message.getChat().getType().equals("private")) {
                    switch (text) {
                        case "/start":
                            text = SpringyBotEnum.CALLBACKS.getText();
                            response.setReplyMarkup(new KeyboardButton().StartReplyKeyboardMarkup());
                            break;
                        case "如何将我添加到您的群组":
                            String groupUrl = "http://t.me/"+this.username+"?startgroup&admin=change_info";
                            text = "Tap on this link and then choose your group.\n"
                                                        + groupUrl
                                                        + "\n\n\"Add admins\" permission is required.";
                            response.setReplyMarkup(new KeyboardButton().addToGroupOrChannelMarkupInline(groupUrl,"group"));
                            break;
                            case "如何将我添加到您的频道":

                            String channelUrl = "http://t.me/"+this.username+"?startchannel&admin=change_info";
                                                        text = "Tap on this link and then choose your channel.\n"
                                                        + channelUrl
                                                        + "\n\n\"Add admins\" permission is required.";

                            response.setReplyMarkup(new KeyboardButton().addToGroupOrChannelMarkupInline(channelUrl,"channel"));
                            break;
                            
                        case "管理面板":
                            break;
                        case "支援团队列表":
                            break;
                        case "管理员设置":
                            break;
                    }
                    this.sendTextMsg(text, chatId.toString(),response);
                }
            }
        }
    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String text, String chatId) {
        SendMessage response = new SendMessage();
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

    @SneakyThrows
    @Async
    public void sendTextMsg(String text, String chatId,SendMessage response) {
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
        System.out.println(getMe());
    }

}