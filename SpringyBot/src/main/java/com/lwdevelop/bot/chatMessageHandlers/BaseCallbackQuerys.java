package com.lwdevelop.bot.chatMessageHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.service.impl.DemandManagementServiceImpl;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public abstract class BaseCallbackQuerys {

    @Autowired
    protected JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(JobManagementServiceImpl.class);

    @Autowired
    protected DemandManagementServiceImpl demandManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(DemandManagementServiceImpl.class);
            
    @Autowired
    protected SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    protected Common common;
    protected Long chatId;
    protected String chatId_str;
    protected CallbackQuery callbackQuery;

    public BaseCallbackQuerys(Common common) {
        this.common = common;
        this.callbackQuery = common.getUpdate().getCallbackQuery();
        this.chatId = common.getUpdate().getCallbackQuery().getFrom().getId();
        this.chatId_str = common.getUpdate().getCallbackQuery().getFrom().getId().toString();
    }

    public abstract void handler();
}
