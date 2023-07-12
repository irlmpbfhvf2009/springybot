package com.lwdevelop.bot.triSpeak.messageHandling;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import com.lwdevelop.bot.utils.Common;


public class CallbackQuerys {
    private Common common;

    public CallbackQuerys(Common common){
        this.common = common;
    }
    
    public void handler() {

        CallbackQuery callbackQuery = common.getUpdate().getCallbackQuery();

        switch (callbackQuery.getData()) {
            
        }

    }
}
