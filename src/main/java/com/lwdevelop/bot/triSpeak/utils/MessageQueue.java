package com.lwdevelop.bot.triSpeak.utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {
    
    private static final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    private static boolean isProcessing = false;

    public static void enqueue(Runnable task) {
        queue.offer(task);
        processQueue();
    }

    private static void processQueue() {
        if (!isProcessing) {
            isProcessing = true;
            while (!queue.isEmpty()) {
                Runnable task = queue.poll();
                task.run();
            }
            isProcessing = false;
        }
    }
}
