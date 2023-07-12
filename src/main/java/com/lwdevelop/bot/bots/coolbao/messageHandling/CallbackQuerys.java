package com.lwdevelop.bot.bots.coolbao.messageHandling;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import com.lwdevelop.bot.bots.utils.Common;

public class CallbackQuerys {
    private Common common;

    public CallbackQuerys(Common common) {
        this.common = common;
    }

    public void handler() {

        CallbackQuery callbackQuery = common.getUpdate().getCallbackQuery();

        switch (callbackQuery.getData()) {
            case "editName":
                String chatId = String.valueOf(callbackQuery.getFrom().getId());
                SendMessage responst = new SendMessage(chatId, "enter name");
                common.executeAsync(responst);
                common.getUserState().put(callbackQuery.getFrom().getId(), "enter_name");
                break;
        }

    }

}
