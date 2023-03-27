package com.lwdevelop.bot.handler.PrivateMessage;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.utils.Common;

public class Job {
    
    private Common common;
    private Message message;
    private String text;
    private SendMessage response;
    private String username;

    public void handler(Common common){
        this.init(common);
        

    }


    private void init(Common common){
        this.message = common.getUpdate().getMessage();
        this.common = common;
        this.common.privateMessageSettings(this.message);
        this.text = message.getText();
        this.response = common.getResponse();
        this.username = common.getUsername();
    }
}
