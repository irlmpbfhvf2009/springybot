package com.lwdevelop.backend;

import java.util.Calendar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.triSpeak.triSpeak_bot;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    private final Long id = 8L;

    @Test
    public void test() {
        SpringyBot springyBot = springyBotServiceImpl.findById(this.id).get();
        SpringyBotDTO dto = new SpringyBotDTO();
        dto.setId(springyBot.getId());
        dto.setUsername(springyBot.getUsername());
        dto.setToken(springyBot.getToken());
        triSpeak_bot bot = new triSpeak_bot(dto);

        ChatPermissions chatPermissions = new ChatPermissions();
        chatPermissions.setCanSendMessages(false);
        chatPermissions.setCanChangeInfo(true);
        chatPermissions.setCanInviteUsers(true);
        chatPermissions.setCanPinMessages(true);
        chatPermissions.setCanSendMediaMessages(true);
        chatPermissions.setCanAddWebPagePreviews(true);
        chatPermissions.setCanSendOtherMessages(true);
        chatPermissions.setCanSendPolls(true);
        // Calendar calendar = Calendar.getInstance();
        // calendar.add(Calendar.MINUTE, 3);
        // int untilDate = (int) (calendar.getTimeInMillis() / 1000);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1);
        int untilDate = (int) (calendar.getTimeInMillis() / 1000);

        RestrictChatMember restrictChatMember = new RestrictChatMember("-911922196", 5855785269L, chatPermissions,
                untilDate);
        try {
            bot.executeAsync(restrictChatMember);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        // try {
        // TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        // botsApi.registerBot(bot);
        // } catch (TelegramApiException e) {
        // e.printStackTrace();
        // }

    }

}
