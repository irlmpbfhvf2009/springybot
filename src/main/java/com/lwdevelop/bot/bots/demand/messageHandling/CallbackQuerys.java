package com.lwdevelop.bot.bots.demand.messageHandling;

import com.lwdevelop.bot.bots.demand.DemandLongPollingBot;
import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.DemandEnum;
import com.lwdevelop.bot.bots.utils.enum_.TelentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.DemandButton;
import com.lwdevelop.dto.*;
import com.lwdevelop.entity.*;
import com.lwdevelop.service.impl.DemandManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CallbackQuerys {

    @Autowired
    private DemandManagementServiceImpl demandManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(DemandManagementServiceImpl.class);
    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private SendMessage response;
    private Common common;
    private CallbackQuery callbackQuery;

    public CallbackQuerys(Common common){
        this.common = common;
        this.callbackQuery = common.getUpdate().getCallbackQuery();
        String chatId = String.valueOf(common.getUpdate().getCallbackQuery().getFrom().getId());
        this.response = new SendMessage();
        this.response.setChatId(chatId);
        this.response.setDisableNotification(false);
        this.response.setDisableWebPagePreview(false);
    }

    public void handler() {

        if (callbackQuery.getData().startsWith(DemandEnum.CLEAR_DEMAND_CQ.getText())) {

            String userId = callbackQuery.getData().substring(DemandEnum.CLEAR_DEMAND_CQ.getText().length(),
                    callbackQuery.getData().lastIndexOf("_"));
            String botId = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);

            // 在这里根据 springyBotId 和 userId 进行相应的清除操作
            Demand demand = demandManagementServiceImpl.findByUserIdAndBotIdWithDemand(userId, botId);
            demandManagementServiceImpl.saveDemand(new DemandDTO().resetDemandFields(demand));

            // // 清除訊息
            Long id = Long.valueOf(demand.getBotId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            DemandLongPollingBot custom = new DemandLongPollingBot(springyBotDTO);

            DemandDTO demandDTO = new DemandDTO().convertToDemandDTO(demand);

            Integer messageId = demand.getLastMessageId();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(DemandEnum.DEMAND_DEFAULT_FORM.getText());

            editMessageText.setReplyMarkup(new DemandButton().keyboard_demand(demandDTO, true));
            try {
                custom.executeAsync(editMessageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = demandManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(demand.getBotId(), userId,
                            DemandEnum.DEMAND_.getText());
            List<GroupMessageIdPostCounts> groupMessageIdPostCounts = demandManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(demand.getBotId(), userId,
                            DemandEnum.DEMAND_.getText());

            channelMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getChannelId()),cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                demandManagementServiceImpl.saveChannelMessageIdPostCounts(cmp);
            });

            groupMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getGroupId()),cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                demandManagementServiceImpl.saveGroupMessageIdPostCounts(cmp);
            });

            this.response.setText(TelentEnum.SUCCESSFULLYDELETED.getText());
            common.executeAsync(this.response);
        } else if (callbackQuery.getData().startsWith(DemandEnum.CLEAR_SUPPLY_CQ.getText())) {

            String userId = callbackQuery.getData().substring(DemandEnum.CLEAR_SUPPLY_CQ.getText().length(),
                    callbackQuery.getData().lastIndexOf("_"));
            String botId = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);
            // 在这里根据 springyBotId 和 userId 进行相应的清除操作
            Supply supply = demandManagementServiceImpl.findByUserIdAndBotIdWithSupply(userId, botId);

            demandManagementServiceImpl.saveSupply(new SupplyDTO().resetSupplyFields(supply));

            // 清除訊息
            Long id = Long.valueOf(supply.getBotId());
            SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
            SpringyBotDTO springyBotDTO = new SpringyBotDTO();
            springyBotDTO.setToken(springyBot.getToken());
            springyBotDTO.setUsername(springyBot.getUsername());
            DemandLongPollingBot custom = new DemandLongPollingBot(springyBotDTO);

            SupplyDTO supplyDTO = new SupplyDTO().convertToSupplyDTO(supply);

            Integer messageId = supply.getLastMessageId();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(DemandEnum.SUPPLY_DEFAULT_FORM.getText());

            editMessageText.setReplyMarkup(new DemandButton().keyboard_supply(supplyDTO, true));
            try {
                custom.executeAsync(editMessageText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = demandManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(supply.getBotId(), userId,
                            DemandEnum.SUPPLY_.getText());
            List<GroupMessageIdPostCounts> groupMessageIdPostCounts = demandManagementServiceImpl
                    .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(supply.getBotId(), userId,
                            DemandEnum.SUPPLY_.getText());
            channelMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getChannelId()),cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                demandManagementServiceImpl.saveChannelMessageIdPostCounts(cmp);
            });

            groupMessageIdPostCounts.stream().forEach(cmp -> {
                DeleteMessage dm = new DeleteMessage(String.valueOf(cmp.getGroupId()),cmp.getMessageId());
                common.executeAsync(dm);
                cmp.setMessageId(-1);
                cmp.setPostCount(0);
                demandManagementServiceImpl.saveGroupMessageIdPostCounts(cmp);
            });

            this.response.setText(TelentEnum.SUCCESSFULLYDELETED.getText());
            common.executeAsync(this.response);
        } else if (callbackQuery.getData().equals(DemandEnum.EDIT_DEMAND_CQ.getText())) {
            response.setText(TelentEnum.REMIND_EDITOR.getText());

            common.executeAsync(this.response);
        } else if (callbackQuery.getData().equals(DemandEnum.EDIT_SUPPLY_CQ.getText())) {
            response.setText(TelentEnum.REMIND_EDITOR.getText());
            common.executeAsync(this.response);
        }

    }

}
