package com.lwdevelop.bot.coolbao.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import com.lwdevelop.bot.coolbao.utils.Common;

public class CallbackQuerys {

    public void handler(Common common) {

        CallbackQuery callbackQuery = common.getUpdate().getCallbackQuery();

        switch (callbackQuery.getData()) {
            case "editName":
                String chatId = String.valueOf(callbackQuery.getFrom().getId());
                SendMessage responst = new SendMessage(chatId, "enter name");
                common.sendResponseAsync(responst);
                common.getUserState().put(callbackQuery.getFrom().getId(), "enter_name");
                break;
        }

    }

}
