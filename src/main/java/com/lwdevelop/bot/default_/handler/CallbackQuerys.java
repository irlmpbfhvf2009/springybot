package com.lwdevelop.bot.default_.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import com.lwdevelop.bot.Common;

public class CallbackQuerys {
    
    public void handler(Common common) {

        CallbackQuery callbackQuery = common.getUpdate().getCallbackQuery();

        switch (callbackQuery.getData()) {
            
        }

    }
}
