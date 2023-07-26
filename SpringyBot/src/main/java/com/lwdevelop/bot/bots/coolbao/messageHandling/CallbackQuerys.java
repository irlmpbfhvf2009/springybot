package com.lwdevelop.bot.bots.coolbao.messageHandling;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.chatMessageHandlers.BaseCallbackQuerys;

public class CallbackQuerys extends BaseCallbackQuerys{

    public CallbackQuerys(Common common) {
        super(common);
    }

    @Override
    public void handler() {
        switch (callbackQuery.getData()) {
            case "editName":
                SendMessage responst = new SendMessage(chatId_str, "enter name");
                common.executeAsync(responst);
                common.getUserState().put(chatId, "enter_name");
                break;
        }

    }

}
