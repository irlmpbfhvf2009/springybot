package com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_;

import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.coolbaoBot.handler.messageEvent.private_.commands.start;
import com.lwdevelop.bot.coolbaoBot.utils.Common;

public class message {
    // private Common common;
    private Message message;
    private String text;

    public void handler(Common common) {
        this.init(common);

        switch (this.text.toLowerCase()) {
            case "/start":
                new start().cmd(common);
                break;

            default:
                this.text = "";
                break;
        }
    }

    private void init(Common common) {
        this.message = common.getUpdate().getMessage();
        // this.common = common;
        this.text = this.message.getText();
    }
}
