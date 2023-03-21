package com.lwdevelop.bot.utils;

public enum SpringyBotEnum {

    // chatType
    CHAT_TYPE_PRIVATE("private"),
    CHAT_TYPE_GROUP("supergroup"),
    CHAT_TYPE_CHANNEL("channel"),


    // KeyboardButton
    HOW_TO_ADD_ME_TO_YOUR_GROUP("如何将我添加到您的群组"),
    ADMIN_PANEL("管理面板"),
    HOW_TO_ADD_ME_TO_YOUR_CHANNEL("如何将我添加到您的频道"),
    SUPPORT_TEAM_LIST("支援团队列表"),
    ADMINISTRATOR_SETTING("管理员设置"),

    CALLBACKS("What con this bot do?\nPlease tap on START");

    private String text;

    private SpringyBotEnum(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
