package com.lwdevelop.bot.talentBot.utils;

public enum SpringyBotEnum {

    // chatType
    CHAT_TYPE_PRIVATE("private"),
    CHAT_TYPE_GROUP("supergroup"),
    CHAT_TYPE_CHANNEL("channel"),

    // command manage 
    HOW_TO_ADD_ME_TO_YOUR_GROUP("如何将我添加到您的群组"),
    ADMIN_PANEL("管理面板"),
    HOW_TO_ADD_ME_TO_YOUR_CHANNEL("如何将我添加到您的频道"),
    SUPPORT_TEAM_LIST("支援团队列表"),
    ADMINISTRATOR_SETTING("管理员设置"),

    // command job 
    EDIT_RECRUITMENT("发布招聘"),
    // POST_RECRUITMENT("发布招聘"),
    EDIT_JOBSEARCH("发布求职"),
    // POST_JOBSEARCH("发布求职"),

    JOB_MANAGEMENT("招聘和求职信息管理"),
    POST_JOB("发布"),

    COMMEND_MANAGE("What con this bot do?\nPlease tap on START"),
    BOT_NOT_ENOUGH_RIGHTS(" 群组的权限设定不足以让机器人有效地管理该群组。\n为了让机器人能够正常运作，请将其设定为该群组的管理员，以便它能够更好地管理该群组。");

    private String text;

    private SpringyBotEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
