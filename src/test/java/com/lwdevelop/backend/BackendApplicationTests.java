package com.lwdevelop.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

	@Test
    void testPostApi() {
        String url = "http://localhost:" + port + "/api/start";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 如果需要在請求中添加身份驗證或其他頭部信息，可以使用以下示例：
        // headers.set("Authorization", "Bearer your-token");

        // 要傳遞的請求主體內容（如果有）
        // String requestBody = "{\"key\":\"value\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        assertEquals(200, responseEntity.getStatusCodeValue());
        // 在此處添加其他斷言或驗證邏輯，例如檢查返回的JSON內容

        // 可以使用responseEntity.getBody()獲取返回的主體內容（如果有）

        // 以下是打印出返回的主體內容的示例：
        // System.out.println(responseEntity.getBody());
    }
	
	@Test
	void contextLoads() {
	}


}
