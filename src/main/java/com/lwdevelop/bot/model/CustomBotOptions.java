package com.lwdevelop.bot.model;

import java.util.Arrays;
import java.util.List;
import org.telegram.telegrambots.bots.DefaultBotOptions;

public class CustomBotOptions extends DefaultBotOptions {
    
    protected List<String> allowedUpdates;

    public CustomBotOptions() {
        // 調用父類別的建構子
        super();
        // 設定自定義的allowedUpdates列表
        setAllowedUpdates(Arrays.asList("update_id", "message", "edited_message",
            "channel_post", "edited_channel_post", "inline_query", "chosen_inline_result",
            "callback_query", "shipping_query", "pre_checkout_query", "poll", "poll_answer",
            "my_chat_member", "chat_member"));
        
    }

}