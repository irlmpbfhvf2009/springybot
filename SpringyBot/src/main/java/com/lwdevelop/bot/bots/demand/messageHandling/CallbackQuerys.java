package com.lwdevelop.bot.bots.demand.messageHandling;

import com.lwdevelop.bot.bots.demand.DemandLongPollingBot;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.TalentEnum;
import com.lwdevelop.bot.chatMessageHandlers.BaseCallbackQuerys;
import com.lwdevelop.dto.DemandDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.dto.SupplyDTO;
import com.lwdevelop.entity.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CallbackQuerys extends BaseCallbackQuerys {

    public CallbackQuerys(Common common) {
        super(common);
    }

    public void handler() {

        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        if (callbackQuery.getData().startsWith("clearDemand_")) {
            clearDemandOrSupply("demand");
        } else if (callbackQuery.getData().startsWith("clearSupply_")) {
            clearDemandOrSupply("supply");
        } else if (callbackQuery.getData().equals("editDemand_")) {
            response.setText(TalentEnum.REMIND_EDITOR.getText());
            common.executeAsync(response);
        } else if (callbackQuery.getData().equals("editSupply_")) {
            response.setText(TalentEnum.REMIND_EDITOR.getText());
            common.executeAsync(response);
        }

    }

    private void clearDemandOrSupply(String type) {

        SpringyBot springyBot = null;
        String userId = "";
        Integer messageId = 0;
        String botId = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);
        List<ChannelMessageIdPostCounts> cmpc = null;
        List<GroupMessageIdPostCounts> gmpc = null;

        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        if (type.equals("demand")) {
            userId = callbackQuery.getData().substring("clearDemand_".length(),
                    callbackQuery.getData().lastIndexOf("_"));

            Demand demand = demandManagementServiceImpl.findByUserIdAndBotIdWithDemand(userId, botId);
            demandManagementServiceImpl.saveDemand(new DemandDTO().resetDemandFields(demand));
            Long id = Long.valueOf(demand.getBotId());
            springyBot = springyBotServiceImpl.findById(id).get();
            messageId = demand.getLastMessageId();

            cmpc = demandManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(demand.getBotId(), userId, type);
            gmpc = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(demand.getBotId(), userId, type);
        } else if (type.equals("supply")) {
            userId = callbackQuery.getData().substring("clearSupply_".length(),
                    callbackQuery.getData().lastIndexOf("_"));

            Supply supply = demandManagementServiceImpl.findByUserIdAndBotIdWithSupply(userId, botId);
            demandManagementServiceImpl.saveSupply(new SupplyDTO().resetSupplyFields(supply));
            Long id = Long.valueOf(supply.getBotId());
            springyBot = springyBotServiceImpl.findById(id).get();
            messageId = supply.getLastMessageId();

            cmpc = demandManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(supply.getBotId(), userId, type);
            gmpc = jobManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(supply.getBotId(), userId, type);
        }
        // // 清除訊息
        if (springyBot != null && cmpc != null && gmpc != null) {
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            DemandLongPollingBot custom = new DemandLongPollingBot(springyBotDTO);
            DeleteMessage deleteMessage = new DeleteMessage(userId, messageId);
            try {
                custom.executeAsync(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            cmpc.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getChannelId()), cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                demandManagementServiceImpl.saveChannelMessageIdPostCounts(cmp);
            });

            gmpc.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getGroupId()), cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                demandManagementServiceImpl.saveGroupMessageIdPostCounts(cmp);
            });

            response.setText(TalentEnum.SUCCESSFULLYDELETED.getText());
            common.executeAsync(response);

        }

    }

}
