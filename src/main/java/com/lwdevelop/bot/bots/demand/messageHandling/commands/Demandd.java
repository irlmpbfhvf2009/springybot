package com.lwdevelop.bot.bots.demand.messageHandling.commands;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.DemandEnum;
import com.lwdevelop.bot.bots.utils.enum_.TalentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.DemandButton;
import com.lwdevelop.bot.bots.utils.keyboardButton.TalentButton;
import com.lwdevelop.bot.chatMessageHandlers.BasePrivateMessage;
import com.lwdevelop.dto.DemandDTO;
import com.lwdevelop.dto.SupplyDTO;
import com.lwdevelop.entity.*;
import com.lwdevelop.service.impl.DemandManagementServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Demandd extends BasePrivateMessage {

    @Autowired
    private DemandManagementServiceImpl demandManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(DemandManagementServiceImpl.class);

    private SupplyDTO supplyDTO;
    private DemandDTO demandDTO;

    public Demandd(Common common) {
        super(common);
        this.supplyDTO = new SupplyDTO(common);
        this.demandDTO = new DemandDTO(common);
        this.saveTgUser(common);
    }

    public void saveTgUser(Common common) {
        String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName()).orElse("");
        String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName()).orElse("");
        String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName()).orElse("");
        SpringyBot springyBot = springyBotServiceImpl.findById(springyBotId).orElseThrow();
        List<TgUser> tgUsers = springyBotServiceImpl.findTgUserBySpringyBotId(common.getSpringyBotId());

        tgUsers.stream().filter(t -> t.getUserId().equals(chatId_str)).findAny()
                .ifPresentOrElse(tu -> {
                    tu.setFirstname(firstname);
                    tu.setLastname(lastname);
                    tu.setUsername(username);
                }, () -> {
                    TgUser tgUser = new TgUser();
                    tgUser.setUserId(chatId_str);
                    tgUser.setFirstname(firstname);
                    tgUser.setLastname(lastname);
                    tgUser.setUsername(username);
                    tgUsers.add(tgUser);
                });
        springyBot.setTgUser(tgUsers);
        springyBotServiceImpl.save(springyBot);

    }

    public void setResponse_demand_management() {

        List<GroupMessageIdPostCounts> gmpcs = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "demand");
        List<ChannelMessageIdPostCounts> cmpcs = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "demand");

        boolean isPostToGroup = gmpcs.stream().anyMatch(gmpc -> gmpc.getPostCount() >= 1);
        boolean isPostToChannel = cmpcs.stream().anyMatch(cmpc -> cmpc.getPostCount() >= 1);

        if (isPostToGroup || isPostToChannel) {
            String text = "您已经发布过[需求]信息，请点选[供需信息管理]进行编辑或删除信息后重新发布。";
            SendMessage response = new SendMessage(chatId_str, text);
            common.executeAsync(response);
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            Demand demand = demandManagementServiceImpl.findByUserIdAndBotIdWithDemand(chatId_str,
                    springyBotId.toString());
            if (demand == null) {
                response.setText(DemandEnum.DEMAND_DEFAULT_FORM.getText());
                log.info("No demand found for user {}, bot id {}", chatId_str, springyBotId);
            } else {
                response.setText(demandDTO.generateDemandResponse(demand, false));
                log.info("demand found for user {}, bot id {}", chatId_str, springyBotId);
            }
            common.executeAsync(response);
            response.setText(TalentEnum.REMIND_EDITOR_.getText());
            common.executeAsync(response);
        }

    }

    public void setResponse_supply_management() {

        List<GroupMessageIdPostCounts> gmpcs = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "supply");
        List<ChannelMessageIdPostCounts> cmpcs = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        springyBotId.toString(), chatId_str, "supply");

        boolean isPostToGroup = gmpcs.stream().anyMatch(gmpc -> gmpc.getPostCount() >= 1);
        boolean isPostToChannel = cmpcs.stream().anyMatch(cmpc -> cmpc.getPostCount() >= 1);

        if (isPostToGroup || isPostToChannel) {
            String text = "您已经发布过[供应]信息，请点选[供需信息管理]进行编辑或删除信息后重新发布。";
            SendMessage response = new SendMessage(chatId_str, text);
            common.executeAsync(response);
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            Supply supply = demandManagementServiceImpl.findByUserIdAndBotIdWithSupply(chatId_str,
                    springyBotId.toString());
            if (supply == null) {
                response.setText(DemandEnum.SUPPLY_DEFAULT_FORM.getText());
                log.info("No supply found for user {}, bot id {}", chatId_str, springyBotId);
            } else {
                response.setText(supplyDTO.generateSupplyResponse(supply, false));
                log.info("supply found for user {}, bot id {}", chatId_str, springyBotId);
            }
            common.executeAsync(response);
            response.setText(TalentEnum.REMIND_EDITOR_.getText());
            common.executeAsync(response);
        }

    }

    public void generateTextDemand(Boolean isEdit) {

        String[] lines = text.split("\\r?\\n");

        TgUser tgUser = springyBotServiceImpl.findTgUserBySpringyBotIdAndUserId(springyBotId, chatId_str);

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();

        List<Demand> demands = demandManagementServiceImpl.findAllByUserIdAndBotIdWithDemand(chatId_str,
                springyBotId.toString());

        Demand demand = demands.stream()
                .filter(d -> d.getUserId().equals(chatId_str) && d.getBotId().equals(springyBotId.toString()))
                .findFirst()
                .orElse(new Demand());

        String isSuccess = demandDTO.fillDemandInfo(demand, lines);

        if (!StringUtils.hasText(isSuccess)) {
            demand.setPublisher(username);
            demand.setBotId(springyBotId.toString());
            demand.setUserId(chatId_str);
            demand.setLastMessageId(message.getMessageId());

            String result = demandDTO.generateDemandDetails(demand);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(springyBotId);
            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(springyBotId);

            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();
            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    response.setChatId(groupId.toString());
                    response.setText(TalentEnum.send_recruitment_text(result));
                    response.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    List<GroupMessageIdPostCounts> gmpcs = demandManagementServiceImpl
                            .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(springyBotId.toString(),
                                    chatId_str, "demand");

                    GroupMessageIdPostCounts gmpc = gmpcs.stream()
                            .filter(g -> g.getGroupId().equals(groupId)
                                    && g.getUserId().equals(chatId_str) && g.getType().equals("demand"))
                            .findFirst().orElse(null);

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(groupId.toString());
                        editMessageText.setText(DemandEnum.send_demand_text(result));
                        editMessageText.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                        editMessageText.setMessageId(gmpc.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);
                        response.setChatId(chatId_str);
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {
                        if (gmpc == null) {
                            final Integer groupMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + groupTitle + " ]发送 [需求] 信息成功");
                            common.executeAsync(response);

                            gmpc = new GroupMessageIdPostCounts();
                            gmpc.setBotId(springyBotId.toString());
                            gmpc.setUserId(chatId_str);
                            gmpc.setGroupId(groupId);
                            gmpc.setGroupTitle(groupTitle);
                            gmpc.setMessageId(groupMessageId);
                            gmpc.setPostCount(1);
                            gmpc.setType("demand");
                        } else {
                            if (gmpc.getPostCount() <= 0) {
                                final Integer groupMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + groupTitle + " ]发送 [需求] 成功");
                                common.executeAsync(response);
                                gmpc.setMessageId(groupMessageId);
                                gmpc.setPostCount(gmpc.getPostCount() + 1);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [需求] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                    final GroupMessageIdPostCounts finalGmpc = gmpc;
                    gmpcs.stream()
                            .filter(g -> g.getGroupId().equals(groupId)
                                    && g.getUserId().equals(chatId_str) && g.getType().equals("demand"))
                            .findFirst()
                            .ifPresentOrElse(
                                    g -> gmpcs.set(gmpcs.indexOf(g), finalGmpc),
                                    () -> gmpcs.add(finalGmpc));
                    demand.setGroupMessageIdPostCounts(gmpcs);
                }
            }
            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    response.setChatId(channelId.toString());
                    response.setText(DemandEnum.send_demand_text(result));
                    response.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    List<ChannelMessageIdPostCounts> cmpcs = demandManagementServiceImpl
                            .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(springyBotId.toString(),
                                    chatId_str,
                                    "demand");
                    ChannelMessageIdPostCounts cmpc = cmpcs.stream().filter(c -> c.getChannelId().equals(channelId)
                            && c.getUserId().equals(chatId_str) && c.getType().equals("demand"))
                            .findFirst().orElse(null);

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(channelId.toString());
                        editMessageText.setText(DemandEnum.send_demand_text(result));
                        editMessageText.setMessageId(cmpc.getMessageId());
                        editMessageText.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);
                        response.setChatId(chatId_str);
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {
                        if (cmpc == null) {
                            final Integer channelMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + channelTitle + " ]发送 [需求] 信息成功");
                            common.executeAsync(response);
                            cmpc = new ChannelMessageIdPostCounts();
                            cmpc.setBotId(springyBotId.toString());
                            cmpc.setUserId(chatId_str);
                            cmpc.setChannelId(channelId);
                            cmpc.setChannelTitle(channelTitle);
                            cmpc.setMessageId(channelMessageId);
                            cmpc.setPostCount(1);
                            cmpc.setType("demand");
                        } else {
                            if (cmpc.getPostCount() <= 0) {
                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + channelTitle + " ]发送 [需求] 信息成功");
                                common.executeAsync(response);
                                cmpc.setMessageId(channelMessageId);
                                cmpc.setPostCount(cmpc.getPostCount() + 1);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [需求] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                    final ChannelMessageIdPostCounts finalCmpc = cmpc;
                    cmpcs.stream()
                            .filter(c -> c.getChannelId().equals(channelId)
                                    && c.getUserId().equals(chatId_str) && c.getType().equals("demand"))
                            .findFirst()
                            .ifPresentOrElse(
                                    c -> cmpcs.set(cmpcs.indexOf(c), finalCmpc),
                                    () -> cmpcs.add(finalCmpc));
                    demand.setChannelMessageIdPostCounts(cmpcs);
                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            response.setText(isSuccess);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
        final Demand finalDemand = demand;
        demands.stream()
                .filter(d -> d.getUserId().equals(chatId_str) && d.getBotId().equals(springyBotId.toString()))
                .findFirst()
                .ifPresentOrElse(
                        d -> demands.set(demands.indexOf(d), finalDemand),
                        () -> demands.add(finalDemand));
        tgUser.setDemand(demands);
        demandManagementServiceImpl.saveTgUser(tgUser);
    }

    public void generateTextSupply(Boolean isEdit) {

        String[] lines = text.split("\\r?\\n");

        TgUser tgUser = springyBotServiceImpl.findTgUserBySpringyBotIdAndUserId(springyBotId, chatId_str);

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();

        List<Supply> supplies = demandManagementServiceImpl.findAllByUserIdAndBotIdWithSupply(chatId_str,
                springyBotId.toString());

        Supply supply = supplies.stream()
                .filter(s -> s.getUserId().equals(chatId_str) && s.getBotId().equals(springyBotId.toString()))
                .findFirst()
                .orElse(new Supply());

        String isSuccess = supplyDTO.fillSupplyInfo(supply, lines);

        if (!StringUtils.hasText(isSuccess)) {
            supply.setPublisher(username);
            supply.setBotId(springyBotId.toString());
            supply.setUserId(chatId_str);
            supply.setLastMessageId(message.getMessageId());

            String result = supplyDTO.generateSupplyDetails(supply);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(springyBotId);
            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(springyBotId);

            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();
            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    response.setChatId(groupId.toString());
                    response.setText(TalentEnum.send_recruitment_text(result));
                    response.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    List<GroupMessageIdPostCounts> gmpcs = demandManagementServiceImpl
                            .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(springyBotId.toString(),
                                    chatId_str, "supply");

                    GroupMessageIdPostCounts gmpc = gmpcs.stream()
                            .filter(g -> g.getGroupId().equals(groupId)
                                    && g.getUserId().equals(chatId_str) && g.getType().equals("supply"))
                            .findFirst().orElse(null);

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(groupId.toString());
                        editMessageText.setText(DemandEnum.send_supply_text(result));
                        editMessageText.setMessageId(gmpc.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        editMessageText.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                        common.executeAsync(editMessageText);
                        response.setChatId(chatId_str);
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {
                        if (gmpc == null) {
                            final Integer groupMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + groupTitle + " ]发送 [供应] 信息成功");
                            common.executeAsync(response);

                            gmpc = new GroupMessageIdPostCounts();
                            gmpc.setBotId(springyBotId.toString());
                            gmpc.setUserId(chatId_str);
                            gmpc.setGroupId(groupId);
                            gmpc.setGroupTitle(groupTitle);
                            gmpc.setMessageId(groupMessageId);
                            gmpc.setPostCount(1);
                            gmpc.setType("supply");
                        } else {
                            if (gmpc.getPostCount() <= 0) {
                                final Integer groupMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + groupTitle + " ]发送 [供应] 成功");
                                common.executeAsync(response);
                                gmpc.setMessageId(groupMessageId);
                                gmpc.setPostCount(gmpc.getPostCount() + 1);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [供应] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                    final GroupMessageIdPostCounts finalGmpc = gmpc;
                    gmpcs.stream()
                            .filter(g -> g.getGroupId().equals(groupId)
                                    && g.getUserId().equals(chatId_str) && g.getType().equals("supply"))
                            .findFirst()
                            .ifPresentOrElse(
                                    g -> gmpcs.set(gmpcs.indexOf(g), finalGmpc),
                                    () -> gmpcs.add(finalGmpc));
                    supply.setGroupMessageIdPostCounts(gmpcs);
                }
            }
            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    response.setChatId(channelId.toString());
                    response.setText(DemandEnum.send_supply_text(result));
                    response.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);

                    List<ChannelMessageIdPostCounts> cmpcs = demandManagementServiceImpl
                            .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(springyBotId.toString(),
                                    chatId_str,
                                    "supply");
                    ChannelMessageIdPostCounts cmpc = cmpcs.stream().filter(c -> c.getChannelId().equals(channelId)
                            && c.getUserId().equals(chatId_str) && c.getType().equals("supply"))
                            .findFirst().orElse(null);

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(channelId.toString());
                        editMessageText.setText(DemandEnum.send_supply_text(result));
                        editMessageText.setMessageId(cmpc.getMessageId());
                        editMessageText.setReplyMarkup(new TalentButton().keyboardJobMarkup());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);
                        response.setChatId(chatId_str);
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {
                        if (cmpc == null) {
                            final Integer channelMessageId = common.executeAsync(response);
                            response.setChatId(chatId_str);
                            response.setText("[ " + channelTitle + " ]发送 [供应] 信息成功");
                            common.executeAsync(response);
                            cmpc = new ChannelMessageIdPostCounts();
                            cmpc.setBotId(springyBotId.toString());
                            cmpc.setUserId(chatId_str);
                            cmpc.setChannelId(channelId);
                            cmpc.setChannelTitle(channelTitle);
                            cmpc.setMessageId(channelMessageId);
                            cmpc.setPostCount(1);
                            cmpc.setType("supply");
                        } else {
                            if (cmpc.getPostCount() <= 0) {
                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(chatId_str);
                                response.setText("[ " + channelTitle + " ]发送 [供应] 信息成功");
                                common.executeAsync(response);
                                cmpc.setMessageId(channelMessageId);
                                cmpc.setPostCount(cmpc.getPostCount() + 1);
                            } else {
                                response.setChatId(chatId_str);
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [供应] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                    final ChannelMessageIdPostCounts finalCmpc = cmpc;
                    cmpcs.stream()
                            .filter(c -> c.getChannelId().equals(channelId)
                                    && c.getUserId().equals(chatId_str) && c.getType().equals("supply"))
                            .findFirst()
                            .ifPresentOrElse(
                                    c -> cmpcs.set(cmpcs.indexOf(c), finalCmpc),
                                    () -> cmpcs.add(finalCmpc));
                    supply.setChannelMessageIdPostCounts(cmpcs);
                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(chatId_str);
            response.setText(isSuccess);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
        final Supply finalSupply = supply;
        supplies.stream()
                .filter(s -> s.getUserId().equals(chatId_str) && s.getBotId().equals(springyBotId.toString()))
                .findFirst()
                .ifPresentOrElse(
                        d -> supplies.set(supplies.indexOf(d), finalSupply),
                        () -> supplies.add(finalSupply));
        tgUser.setSupply(supplies);
        demandManagementServiceImpl.saveTgUser(tgUser);
    }

    public void setResponse_edit_demand_management() {

        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(springyBotId.toString(),
                        chatId_str, "demand");

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 需求 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(springyBotId.toString(),
                        chatId_str, "demand");

        List<String> alertMessages_group = groupMessageIdPostCounts.stream().map(gmpc -> {

            String markdown = "群组 [ " + gmpc.getGroupTitle() + " ] ";
            if (gmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + gmpc.getPostCount() + " 则 [ 需求 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        String alert_channel = String.join("", alertMessages_channel);
        String alert_group = String.join("", alertMessages_group);
        String alert = alert_channel + alert_group;

        if (!alert.isEmpty()) {
            response.setText("通知：\n" + alert + "\n下方模版可对频道内信息进行编辑和删除操作");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            List<TgUser> tgUsers = springyBotServiceImpl.findTgUserBySpringyBotId(springyBotId);

            TgUser tgUser = tgUsers.stream().filter(tu -> tu.getUserId().equals(chatId_str)).findAny().get();

            List<Demand> demands = demandManagementServiceImpl.findAllByUserIdAndBotIdWithDemand(chatId_str,
                    springyBotId.toString());

            demands.stream()
                    .filter(d -> d.getUserId().equals(chatId_str) && d.getBotId().equals(springyBotId.toString()))
                    .findFirst().ifPresentOrElse(demand -> {
                        DemandDTO demandDTO = new DemandDTO(chatId_str, springyBotId.toString());
                        response.setText(demandDTO.generateDemandResponse(demand, true));
                        demandDTO = demandDTO.createDemandDTO(chatId_str, springyBotId.toString());

                        response.setReplyMarkup(new DemandButton().keyboard_demand(demandDTO, true));
                        response.setDisableWebPagePreview(true);
                        Integer messageId = common.executeAsync(response);
                        demand.setLastMessageId(messageId);
                    }, () -> {
                        DemandDTO demandDTO = new DemandDTO(chatId_str, springyBotId.toString());
                        response.setText(DemandEnum.DEMAND_EDITOR_DEFAULT_FORM.getText());
                        response.setReplyMarkup(new DemandButton().keyboard_demand(demandDTO, false));
                        response.setDisableWebPagePreview(true);
                        Demand demand = new Demand(chatId_str, springyBotId.toString(),
                                common.executeAsync(response));
                        demands.add(demand);
                    });
            tgUser.setDemand(demands);
            demandManagementServiceImpl.saveTgUser(tgUser);
        } else {
            response.setText("未发布需求");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

    // 供应
    public void setResponse_edit_supply_management() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId_str);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(springyBotId.toString(),
                        chatId_str, "supply");

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 供应 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(springyBotId.toString(),
                        chatId_str, "supply");

        List<String> alertMessages_group = groupMessageIdPostCounts.stream().map(gmpc -> {

            String markdown = "群组 [ " + gmpc.getGroupTitle() + " ] ";
            if (gmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + gmpc.getPostCount() + " 则 [ 供应 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        String alert_channel = String.join("", alertMessages_channel);
        String alert_group = String.join("", alertMessages_group);
        String alert = alert_channel + alert_group;

        if (!alert.isEmpty()) {
            response.setText("通知：\n" + alert + "\n下方模版可对频道内信息进行编辑和删除操作");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            List<TgUser> tgUsers = springyBotServiceImpl.findTgUserBySpringyBotId(springyBotId);

            TgUser tgUser = tgUsers.stream().filter(tu -> tu.getUserId().equals(chatId_str)).findAny().get();

            List<Supply> supplies = demandManagementServiceImpl.findAllByUserIdAndBotIdWithSupply(chatId_str,
                    springyBotId.toString());

            supplies.stream()
                    .filter(d -> d.getUserId().equals(chatId_str) && d.getBotId().equals(springyBotId.toString()))
                    .findFirst().ifPresentOrElse(supply -> {
                        SupplyDTO supplyDTO = new SupplyDTO(chatId_str, springyBotId.toString());
                        response.setText(supplyDTO.generateSupplyResponse(supply, true));
                        supplyDTO = supplyDTO.createSupplyDTO(chatId_str, springyBotId.toString());

                        response.setReplyMarkup(new DemandButton().keyboard_supply(supplyDTO, true));
                        response.setDisableWebPagePreview(true);
                        Integer messageId = common.executeAsync(response);
                        supply.setLastMessageId(messageId);
                    }, () -> {
                        SupplyDTO supplyDTO = new SupplyDTO(chatId_str, springyBotId.toString());
                        response.setText(DemandEnum.SUPPLY_EDITOR_DEFAULT_FORM.getText());
                        response.setReplyMarkup(new DemandButton().keyboard_supply(supplyDTO, false));
                        response.setDisableWebPagePreview(true);
                        Supply supply = new Supply(chatId_str, springyBotId.toString(),
                                common.executeAsync(response));
                        supplies.add(supply);
                    });
            tgUser.setSupply(supplies);
            demandManagementServiceImpl.saveTgUser(tgUser);
        } else {
            response.setText("未发布供应");
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

    @Override
    public void handler() {
        throw new UnsupportedOperationException("Unimplemented method 'handler'");
    }

}
