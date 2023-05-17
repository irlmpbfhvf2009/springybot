package com.lwdevelop.bot.triSpeak.utils;

public enum SpringyBotEnum {
    CHAT_TYPE_CHANNEL("channel");

    private String text;

    private SpringyBotEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static String warning_text(String username,String channel_title){
        return "@" + username + " : 关注频道 " + channel_title + " 后才能发言";
    }
}
