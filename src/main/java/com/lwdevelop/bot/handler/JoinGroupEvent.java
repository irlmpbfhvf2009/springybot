package com.lwdevelop.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class JoinGroupEvent {
    public void handler(Message message, String username) {

        System.out.println(message);
        // 邀請人
        message.getFrom().getId();
        message.getFrom().getFirstName();
        message.getFrom().getUserName();

        // 被邀請對象
        for (User member : message.getNewChatMembers()) {
            // User(id=5855785269, firstName=Skeddy, isBot=true, lastName=null,
            // userName=CCP1121_BOT, languageCode=null,
            // canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null)
            if (username.equals(member.getUserName()) && member.getIsBot()) {

            }
        }
    }
}
