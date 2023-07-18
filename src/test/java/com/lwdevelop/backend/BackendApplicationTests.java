package com.lwdevelop.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    }

}
