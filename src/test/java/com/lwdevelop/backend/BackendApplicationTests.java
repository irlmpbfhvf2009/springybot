package com.lwdevelop.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.SpringyBotRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    SpringyBotRepository springyBotRepository;


    @Test
    public void test() {
        SpringyBot springyBot= springyBotRepository.findById(1L).get();
        Long id = springyBot.getBotId();
        System.out.println(id);
    }

}
