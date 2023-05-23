package com.lwdevelop.bot.triSpeak.utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import com.lwdevelop.bot.Common;

public class MessageTaskExecutor {

    private ExecutorService executorService;
    private Common common;

    public MessageTaskExecutor(Common common) {
        executorService = Executors.newFixedThreadPool(10);
        this.common = common;
    }

    public void executeDeleteMessageTask(List<DeleteMessage> deleteSystemMessages, int second) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (DeleteMessage deleteMessage : deleteSystemMessages) {
                    executorService.execute(() -> common.executeAsync(deleteMessage));
                }
            }
        }, second);
    }

    public void shutdown() {
        executorService.shutdown();
    }

}
