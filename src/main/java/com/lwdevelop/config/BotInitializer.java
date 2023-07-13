package com.lwdevelop.config;

import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import com.lwdevelop.bot.factory.BotFactory;
import com.lwdevelop.bot.model.CustomLongPollingBot;
import com.lwdevelop.entity.Config;
import com.lwdevelop.entity.InvitationThreshold;
import com.lwdevelop.entity.RecordChannelUsers;
import com.lwdevelop.entity.RecordGroupUsers;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BotInitializer {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private BotFactory botFactory;

    @Autowired
    private TelegramBotsApi telegramBotsApi;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    protected List<BotSession> botSessions;

    @PostConstruct
    public void startBot() {
        springyBotServiceImpl.findAll().forEach(springybot -> {
            try {
                Long id = springybot.getId();
                String botType = springybot.getBotType();
                String botModel = springybot.getBotModel();
                CustomLongPollingBot longPollingBot = null;

                switch (botModel) {
                    case "LongPolling":
                        switch (botType) {
                            case "talent":
                                longPollingBot = botFactory.createTalentLongPollingBot(springybot);
                                break;
                            case "coolbao":
                                longPollingBot = botFactory.createCoolbaoLongPollingBot(springybot);
                                break;
                            case "triSpeak":
                                longPollingBot = botFactory.createTriSpeakLongPollingBot(springybot);
                                break;
                            default:
                                break;
                        }
                        break;
                    case "Webhook":
                        switch (botType) {
                            case "coolbao":
                                break;
                        }
                        break;
                }

                if (longPollingBot != null) {
                    Long botId = longPollingBot.getMe().getId();
                    springybot.setBotId(botId);
                    springybot.setState(true);
                    BotSession botSession = telegramBotsApi.registerBot(longPollingBot);
                    this.botSessions.add(botSession);
                    // Redis
                    Config config = springyBotServiceImpl.findById(id).get().getConfig();
                    List<RecordGroupUsers> recordGroupUsers = springyBotServiceImpl
                            .findRecordGroupUsersBySpringyBotId(id);
                    List<RecordChannelUsers> recordChannelUsers = springyBotServiceImpl
                            .findRecordChannelUsersBySpringyBotId(id);
                    List<InvitationThreshold> invitationThreshold = springyBotServiceImpl
                            .findInvitationThresholdBySpringyBotId(id);
                    redisUtils.set("Config_" + id, config);
                    redisUtils.set("RecordGroupUsers_" + id, recordGroupUsers);
                    redisUtils.set("RecordChannelUsers_" + id, recordChannelUsers);
                    redisUtils.set("InvitationThreshold_" + id, invitationThreshold);

                    log.info("{} Telegram bot started.", springybot.getUsername());
                }
            } catch (TelegramApiException e) {
                log.error("Catch TelegramApiException: {}", e.toString());
                springybot.setState(false);
            } catch (NoSuchElementException e) {
                log.error("Catch NoSuchElementException: {}", e.toString());
                springybot.setState(false);
            }
            springyBotServiceImpl.save(springybot);
        });
    }
}
