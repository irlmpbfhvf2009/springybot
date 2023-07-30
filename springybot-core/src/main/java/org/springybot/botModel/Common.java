package org.springybot.botModel;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Common extends AbsSender {

    private Long springyBotId;

    private Long botId;

    private String botUsername;

    private Update update;

    // 用来存储用户的状态(会话)
    private HashMap<Long, String> userState;
    private HashMap<Long, List<String>> groupMessageMap;
    private HashMap<Long, List<String>> groupMessageMap2;

    @Async
    public void deleteMessageTask(DeleteMessage deleteSystemMessage, int second) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                executeAsync(deleteSystemMessage);
            }
        }, second * 1000);
    }

}
