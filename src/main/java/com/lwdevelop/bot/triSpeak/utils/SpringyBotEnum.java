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
}
