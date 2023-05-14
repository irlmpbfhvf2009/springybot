package com.lwdevelop.bot.triSpeak.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import com.lwdevelop.bot.triSpeak.utils.Common;


public class CallbackQuerys {
    
    public void handler(Common common) {

        CallbackQuery callbackQuery = common.getUpdate().getCallbackQuery();

        switch (callbackQuery.getData()) {
            
        }

    }
}
