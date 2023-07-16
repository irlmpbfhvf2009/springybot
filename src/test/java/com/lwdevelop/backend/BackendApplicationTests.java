package com.lwdevelop.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.lwdevelop.bot.bots.talent.TalentLongPollingBot;
import com.lwdevelop.bot.bots.utils.keyboardButton.TelentButton;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.repository.SpringyBotRepository;
import com.lwdevelop.utils.RedisUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

    @Autowired
    SpringyBotRepository springyBotRepository;

    @Autowired
    RedisUtils redisUtils;

    @Test
    public void test() {
        String s = "招聘人才";
        SendMessage response = new SendMessage();
        response.setChatId("-1001736994958");
        response.setText(s);
        response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
        response.setDisableWebPagePreview(true);
        SpringyBotDTO dto = new SpringyBotDTO();
        dto.setToken("6284800934:AAGPm5yx-5pD_aWELJzw2a8cOMHj0n2XdGo");
        TalentLongPollingBot a = new TalentLongPollingBot(dto);
        try {
            a.execute(response);
        } catch (TelegramApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
