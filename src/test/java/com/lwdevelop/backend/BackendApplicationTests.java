package com.lwdevelop.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import com.lwdevelop.bot.talent.talent_bot;
import com.lwdevelop.dto.SpringyBotDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
    @Mock
    private TelegramBotsApi telegramBotsApi;

    @Mock
    private LongPollingBot bot;


	@Test
	void testBotMethod() {
		String token = "5855785269:AAH9bvPpYudd2wSAvMnBTiKakCeoB92_Z_8";
		SpringyBotDTO springyBotDTO = new SpringyBotDTO();
		springyBotDTO.setToken(token);
		try {
			LongPollingBot bot = new talent_bot(springyBotDTO);
			telegramBotsApi.registerBot(bot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void contextLoads() {
		assertNotNull(restTemplate);
	}

	@Test
	void testAPIEndpoint() {
		// 测试应用程序的某个API端点
		String api = "/debug/addConfig";
		String url = "http://localhost:" + port + "/api" + api;
		String response = restTemplate.getForObject(url, String.class);

		// 断言API响应是否符合预期
        assertEquals("Expected response", response);
	}


}
