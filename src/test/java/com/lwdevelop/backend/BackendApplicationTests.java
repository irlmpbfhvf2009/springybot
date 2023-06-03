package com.lwdevelop.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdevelop.bot.triSpeak.triSpeak_bot;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    private static int i = 0;
    @Test
    void testRes() {
        Long id = 61L;
        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        SpringyBotDTO springyBotDTO = new SpringyBotDTO();
        springyBotDTO.setId(id);
        springyBotDTO.setUsername(springyBot.getUsername());
        springyBotDTO.setToken(springyBot.getToken());
        triSpeak_bot bot = new triSpeak_bot(springyBotDTO);
        // TelegramBotsApi botsApi = Mockito.mock(TelegramBotsApi.class);

        ChatPermissions chatPermissions = new ChatPermissions();
        chatPermissions.setCanSendMessages(true);
        chatPermissions.setCanChangeInfo(true);
        chatPermissions.setCanInviteUsers(true);
        chatPermissions.setCanPinMessages(true);
        chatPermissions.setCanSendMediaMessages(true);
        chatPermissions.setCanAddWebPagePreviews(true);
        chatPermissions.setCanSendOtherMessages(true);
        chatPermissions.setCanSendPolls(true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1); // 将当前时间增加1秒
        Integer time = (int)  calendar.getTimeInMillis() / 1000;
        
        
        RestrictChatMember restrictChatMember = new RestrictChatMember("-1001332060757", 5036779522L, chatPermissions,
                time); 
        springyBot.getRestrictMember().forEach(a->{
            i++;
            if(a.getStatus()){
                restrictChatMember.setChatId(a.getChatId());
                restrictChatMember.setUserId(a.getUserId());
                try {
                    System.out.println("執行任務第 "+i+" 次 chatid: " +a.getChatId()+" userId: "+a.getUserId());
                    bot.execute(restrictChatMember);
                } catch (TelegramApiException e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }
            }
        });
        System.out.println("完成任務");
        springyBotServiceImpl.save(springyBot);



    }
    @Test
    void testPostApiq() {
        Long id = 60L;
        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        springyBot.getRestrictMember().forEach(a->{
            i++;
            a.setStatus(false);
            System.out.println("執行任務第 "+i+" 次 chatid: " +a.getChatId()+" userId: "+a.getUserId());
        });
        System.out.println("完成任務");
        springyBotServiceImpl.save(springyBot);
    }

    @Test
    void testPostApi() {

        String loginUrl = "http://localhost:" + port + "/api/admins/login";

        // 构建请求体参数
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", "admin");
        body.add("password", "123456");

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 发送POST请求
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl,
                new HttpEntity<>(body, headers),
                String.class);

        // 获取响应结果
        String responseBody = response.getBody();
        // 示例断言：验证返回的状态码是否为200
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 使用Jackson解析JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson;
        String token = "";
        try {
            responseJson = objectMapper.readTree(responseBody);
            // 提取token字段的值
            token = responseJson.get("data").get("token").asText();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (token == "") {
            System.out.println("token 獲取失敗 ");
        } else {
            // 打印token
            System.out.println("Token: " + token);

            String url = "http://localhost:" + port + "/api/springybot/start";

            HttpHeaders headers_ = new HttpHeaders();
            headers_.setContentType(MediaType.APPLICATION_JSON);

            // 如果需要在請求中添加身份驗證或其他頭部信息，可以使用以下示例：
            headers_.set("Authorization", "Bearer " + token);

            // 要傳遞的請求主體內容（如果有）

            String requestBody = "{\"id\":\"1\",\"token\":\"" 
                    + "\",\"username\":\"test\",\"state\":\"true\",\"botType\":\"coolbao\"}";

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers_);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url,
                    HttpMethod.POST, requestEntity,
                    String.class);

            assertEquals(200, responseEntity.getStatusCodeValue());
            // 在此處添加其他斷言或驗證邏輯，例如檢查返回的JSON內容

            // 可以使用responseEntity.getBody()獲取返回的主體內容（如果有）

            // 以下是打印出返回的主體內容的示例：
            System.out.println(responseEntity.getBody());

        }

    }

}
