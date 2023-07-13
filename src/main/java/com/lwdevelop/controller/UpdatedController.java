package com.lwdevelop.controller;

import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.telegram.telegrambots.meta.generics.BotSession;

import com.lwdevelop.config.BotInitializer;

@Controller
public class UpdatedController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BotInitializer botInitializer;

    @Autowired
    private List<BotSession> botSessions;

    @ResponseBody
    @PostMapping("/reloadInstance")
    public void reloadInstance() {
        botSessions.forEach(botSession->{
            botSession.stop();
        });
        botSessions = new ArrayList<>();
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext
                .getAutowireCapableBeanFactory();
        defaultListableBeanFactory.destroySingleton("botInitializer");
        defaultListableBeanFactory.registerSingleton("botInitializer", new BotInitializer());
        botInitializer.startBot(); // 启动新的Bot实例
    }

}