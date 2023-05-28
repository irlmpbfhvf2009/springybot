package com.lwdevelop.bot.triSpeak.utils;

import java.util.LinkedList;
import java.util.Queue;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageQueue {
    private Queue<SendMessage> queue;

    public MessageQueue() {
        this.queue = new LinkedList<>();
    }

    public void push(SendMessage message) {
        queue.offer(message);
    }

    public SendMessage pop() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
