package com.lwdevelop.bot.utils;

public class CommonUtils {

    public boolean chatTypeIsPrivate(String type) {
        return type.equals(SpringyBotEnum.CHAT_TYPE_PRIVATE.getText()) ? true : false;
    }

    public boolean chatTypeIsGroup(String type) {
        return type.equals(SpringyBotEnum.CHAT_TYPE_GROUP.getText()) ? true : false;
    }

    public boolean chatTypeIsChannel(String type) {
        return type.equals(SpringyBotEnum.CHAT_TYPE_CHANNEL.getText()) ? true : false;
    }

    public String howToAddForText(String username, String url, String type) {
        return "Tap on this link and then choose your " + type + ".\n" + url
                + "\n\n\"Add admins\" permission is required.";
    }

    public String getUrl(String type, String username) {
        return type == "group" ? "http://t.me/" + username + "?startgroup&admin=change_info"
                : "http://t.me/" + username + "?startchannel&admin=change_info";
    }
}
