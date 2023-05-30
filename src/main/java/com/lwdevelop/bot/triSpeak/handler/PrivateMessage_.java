package com.lwdevelop.bot.triSpeak.handler;

import java.util.HashMap;

import com.lwdevelop.bot.Common;

public class PrivateMessage_ {

    private Common common;
    private String text;

    public PrivateMessage_(Common common){
        this.common = common;
        this.text = common.getUpdate().getMessage().getText();
    }

    public void handler() {

        switch (this.text) {
            case "/reset_groupMessageMap":
                this.common.setGroupMessageMap(new HashMap<>());
                break;
            case "/reset_config":
                this.common.setConfigDTO_map(new HashMap<>());
                break;
        
            default:
                break;
        }
        
    }
    
}
