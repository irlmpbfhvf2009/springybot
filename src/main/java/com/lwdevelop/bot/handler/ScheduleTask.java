package com.lwdevelop.bot.handler;

import com.lwdevelop.bot.Custom;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.Advertise;
import com.lwdevelop.entity.SpringyBot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Component
@Configuration
public class ScheduleTask implements SchedulingConfigurer {
    @Value("${printTime.cron}")
    private String cron;
    //判断是否执行发送讯息
    private Boolean run = false;

    //请求传过来的groupId
    private Long groupId;
    //判断是否加入bot
    private Boolean addBot = false;

    private ArrayList<Advertise> advertises = new ArrayList<>();
    //用来判断群组发送消息是否有运行
    private Set<Long> groupIds = new HashSet<>();

    private SpringyBot springyBot;

    //停止发送方法传送过来的botId
    private Long botId ;
    //将发送过来的新bot加入到集合中
    private Set<SpringyBot> bots= new HashSet<>();


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
// 动态使用cron表达式设置循环间隔
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                if (run) {
                    if (addBot){
                        bots.add(springyBot);
                    }
                    //遍历bot
                    System.out.println(bots);
                for (SpringyBot springyBot : bots) {
                    stop();

                                SpringyBotDTO springyBotDTO = new SpringyBotDTO();
                                springyBotDTO.setToken(springyBot.getToken());
                                springyBotDTO.setUsername(springyBot.getUsername());
                                Custom custom = new Custom(springyBotDTO);
                                groupIds.add(groupId);
                                //遍历广告
                                for (Advertise advertise : advertises) {
                                    stop();

                                    //遍历群组
                                    for (Long groupId : groupIds) {
                                        try {
                                            //停止判断
                                                stop();
                                            custom.common.sendMsg(groupId.toString(), advertise.getContact(), advertise.getPath());

                                        } catch (TelegramApiException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    try {
                                        //广告间隔时间
                                        Thread.sleep(advertise.getDeilyTime() * 1000L);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
        }, triggerContext -> {
            // 使用CronTrigger触发器，可动态修改cron表达式来操作循环规则
            CronTrigger cronTrigger = new CronTrigger(cron);
            Date nextExecutionTime = cronTrigger.nextExecutionTime(triggerContext);
            return nextExecutionTime;
        });

    }

    public void stop(){
        if (springyBot.getId().equals(botId)){
            bots.remove(springyBot);
            setAddBot(false);
            botId =null;
            if (bots.isEmpty()){
                setRun(false);
            }
        }
    }
}

