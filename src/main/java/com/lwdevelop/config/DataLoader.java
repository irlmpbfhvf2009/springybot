package com.lwdevelop.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.lwdevelop.entity.Admin;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.AdminServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private RedisUtils redisUtils;

    @Async
    @Override
    public void run(String... args) throws Exception {
        
        redisUtils.clearAllData();

        try {
            List<SpringyBot> springyBots = springyBotServiceImpl.findAll();
            springyBots.forEach(springyBot -> {
                springyBot.setState(false);
                springyBotServiceImpl.save(springyBot);
            });
            Admin admin = adminServiceImpl.findByUsername("admin");
            Admin test = adminServiceImpl.findByUsername("test");
            if (admin == null) {
                admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword("123456");
                admin.setEnabled(true);
                admin.setRoles(Arrays.asList(new String[] { "ADMIN" }));
                adminServiceImpl.saveAdmin(admin);
            }

            if (test == null) {
                test = new Admin();
                test.setUsername("test");
                test.setPassword("123456");
                test.setEnabled(true);
                test.setRoles(Arrays.asList(new String[] { "ADMIN", "TEST" }));
                adminServiceImpl.saveAdmin(test);
            }

        } catch (Exception e) {
            throw e;
        }

    }

}