package com.lwdevelop.bot.default_.utils;

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
