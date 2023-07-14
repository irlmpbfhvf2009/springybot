package com.lwdevelop.bot.bots.demand.messageHandling.commands;

import com.lwdevelop.bot.bots.utils.Common;
import com.lwdevelop.bot.bots.utils.enum_.DemandEnum;
import com.lwdevelop.bot.bots.utils.enum_.TelentEnum;
import com.lwdevelop.bot.bots.utils.keyboardButton.DemandButton;
import com.lwdevelop.bot.bots.utils.keyboardButton.TelentButton;
import com.lwdevelop.dto.DemandDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.SupplyDTO;
import com.lwdevelop.entity.*;
import com.lwdevelop.service.impl.DemandManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Demandd {

    @Autowired
    private DemandManagementServiceImpl demandManagementServiceImpl = SpringUtils.getApplicationContext()
            .getBean(DemandManagementServiceImpl.class);

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
            .getBean(SpringyBotServiceImpl.class);

    private SupplyDTO supplyDTO;
    private DemandDTO demandDTO;

    public Demandd() {

    }

    public Demandd(SupplyDTO supplyDTO, DemandDTO demandDTO) {
        this.supplyDTO = supplyDTO;
        this.demandDTO = demandDTO;
    }

    public void saveDemandUser(Common common) {
        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
        String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName()).orElse("");
        String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName()).orElse("");
        String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName()).orElse("");
        SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
        List<DemandUser> demandUserList = springyBotServiceImpl.findDemandUserBySpringyBotId(common.getSpringyBotId());

        demandUserList.stream().filter(j -> j.getUserId().equals(userId)).findAny()
                .ifPresentOrElse(ju -> {
                    ju.setFirstname(firstname);
                    ju.setLastname(lastname);
                    ju.setUsername(username);
                }, () -> {
                    DemandUser demandUser = new DemandUser();
                    demandUser.setUserId(userId);
                    demandUser.setFirstname(firstname);
                    demandUser.setLastname(lastname);
                    demandUser.setUsername(username);
                    demandUserList.add(demandUser);
                });
        springyBot.setDemandUser(demandUserList);
        springyBotServiceImpl.save(springyBot);

    }

    public void setResponse_demand_management(Common common) {

        log.info("Entering setResponse_demand_management method...");

        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        String.valueOf(id), userId, DemandEnum.DEMAND_.getText());
        channelMessageIdPostCounts.stream().filter(cmpc -> cmpc.getPostCount() >= 1).findAny()
                .ifPresentOrElse(action -> {
                    SendMessage response = new SendMessage(userId, DemandEnum.ALREADY_POST_DEMAND.getText());
                    common.executeAsync(response);
                }, () -> {

                    SendMessage response = new SendMessage();
                    response.setChatId(userId);

                    Demand demand = demandManagementServiceImpl.findByUserIdAndBotIdWithDemand(userId,
                            String.valueOf(id));

                    // 沒有發布過
                    if (demand == null) {
                        response.setText(DemandEnum.DEMAND_DEFAULT_FORM.getText());
                        log.info("No demand found for user {}, bot id {}", userId, id);
                    } else {
                        response.setText(demandDTO.generateDemandResponse(demand, false));
                        log.info("Demand found for user {}, bot id {}: {}", userId, id, demand);
                    }

                    common.executeAsync(response);
                    response.setText(TelentEnum.REMIND_EDITOR_.getText());
                    common.executeAsync(response);
                });

    }

    public void setResponse_supply_management(Common common) {
        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        String.valueOf(id), userId, DemandEnum.SUPPLY_.getText());
        channelMessageIdPostCounts.stream().filter(cmpc -> cmpc.getPostCount() >= 1).findAny()
                .ifPresentOrElse(action -> {
                    SendMessage response = new SendMessage(userId, DemandEnum.ALREADY_POST_SUPPLY.getText());
                    common.executeAsync(response);
                }, () -> {
                    log.info("Entering setResponse_supply_management method...");

                    SendMessage response = new SendMessage();
                    response.setChatId(userId);

                    Supply supply = demandManagementServiceImpl.findByUserIdAndBotIdWithSupply(userId,
                            String.valueOf(id));

                    if (supply == null) {
                        response.setText(DemandEnum.SUPPLY_DEFAULT_FORM.getText());
                        log.info("No supply found for user {}, bot id {}", userId, id);
                    } else {

                        response.setText(supplyDTO.generateSupplyResponse(supply, false));
                        log.info("Supply found for user {}, bot id {}: {}", userId, id, supply);

                    }

                    common.executeAsync(response);
                    response.setText(TelentEnum.REMIND_EDITOR_.getText());
                    common.executeAsync(response);
                });

    }

    public void generateTextDemand(Common common, Boolean isEdit) {
        Message message = common.getUpdate().getMessage();
        String text = message.getText();
        // 将文本内容按行分割成字符串数组
        String[] lines = text.split("\\r?\\n");

        Demand demand = demandManagementServiceImpl.findByUserIdAndBotIdWithDemand(
                String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

        if (demand == null) {
            demand = new Demand();
        }

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();
        demand.setPublisher(username);

        String isSuccess = demandDTO.fillDemandInfo(demand, lines);

        if (!StringUtils.hasText(isSuccess)) {
            demand.setBotId(String.valueOf(common.getSpringyBotId()));
            demand.setUserId(String.valueOf(message.getChatId()));
            demand.setLastMessageId(message.getMessageId());
            // 處理資料表
            Long id = common.getSpringyBotId();
            String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
            List<DemandUser> demandUsers = springyBotServiceImpl.findDemandUserBySpringyBotId(id);
            DemandUser demandUser = demandUsers.stream().filter(j -> j.getUserId().equals(userId)).findAny().get();
            final Long final_demandId = demand.getId();

            if (!demandUser.getDemand().stream().anyMatch(p -> p.getId().equals(final_demandId))) {
                demandUser.getDemand().add(demand);
            }

            demandManagementServiceImpl.saveDemand(demand);

            String result = demandDTO.generateDemandDetails(demand);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(common.getSpringyBotId());

            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();

            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    String channelLink = robotChannelManagement.getLink();
                    response.setChatId(String.valueOf(channelId));
                    response.setText(DemandEnum.send_demand_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableWebPagePreview(true);
                    response.setDisableNotification(true);
                    ChannelMessageIdPostCounts channelMessageIdPostCounts = demandManagementServiceImpl
                            .findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                                    channelId, String.valueOf(message.getChatId()),
                                    DemandEnum.DEMAND_.getText());

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText(DemandEnum.send_demand_text(result));
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(demand.getUserId());
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {

                        if (channelMessageIdPostCounts == null) {
                            final Integer channelMessageId = common.executeAsync(response);

                            response.setChatId(demand.getUserId());
                            response.setText("[ " + channelTitle + " ]发送成功");
                            common.executeAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(demand.getBotId());
                            channelMessageIdPostCounts.setUserId(demand.getUserId());
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType(DemandEnum.DEMAND_.getText());
                            demand = demandManagementServiceImpl.findByUserIdAndBotIdWithDemand(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));
                            demand.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            demandManagementServiceImpl.saveDemand(demand);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() <= 0) {
                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(demand.getUserId());
                                response.setText("[ " + channelTitle + " ]发送 [需求] 信息成功");
                                common.executeAsync(response);
                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                demandManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(demand.getUserId());
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [需求] 信息");
                                common.executeAsync(response);
                            }
                        }

                    }

                }
            }

            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(common.getSpringyBotId());

            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    String groupLink = robotGroupManagement.getLink();
                    response.setChatId(String.valueOf(groupId));
                    response.setText(DemandEnum.send_demand_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    GroupMessageIdPostCounts groupMessageIdPostCounts = demandManagementServiceImpl
                            .findByGroupIdAndTypeWithGroupMessageIdPostCounts(groupId,
                                    DemandEnum.DEMAND_.getText());
                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(groupId));
                        editMessageText.setText(DemandEnum.send_demand_text(result));
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(demand.getUserId());
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {

                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.executeAsync(response);

                            response.setChatId(demand.getUserId());
                            response.setText("[ " + groupTitle + " ]发送 [需求] 信息成功");
                            common.executeAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(demand.getBotId());
                            groupMessageIdPostCounts.setUserId(demand.getUserId());
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType(DemandEnum.DEMAND_.getText());
                            demand = demandManagementServiceImpl.findByUserIdAndBotIdWithDemand(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));
                            demand.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            demandManagementServiceImpl.saveDemand(demand);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {
                                final Integer groupMessageId = common.executeAsync(response);

                                response.setChatId(demand.getUserId());
                                response.setText("[ " + groupTitle + " ]发送 [需求] 成功");
                                common.executeAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                demandManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(demand.getUserId());
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [需求] 信息");
                                common.executeAsync(response);
                            }
                        }

                    }

                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(demand.getUserId());
            response.setText(isSuccess);
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

    public void generateTextSupply(Common common, Boolean isEdit) {

        Message message = common.getUpdate().getMessage();
        String text = message.getText();

        String[] lines = text.split("\\r?\\n");

        Supply supply = demandManagementServiceImpl.findByUserIdAndBotIdWithSupply(
                String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

        if (supply == null) {
            // 清除舊資料
            supply = new Supply();
        }

        String username = "@" + common.getUpdate().getMessage().getFrom().getUserName();
        supply.setPublisher(username);

        String isSuccess = supplyDTO.fillSupplyInfo(supply, lines);
        if (!StringUtils.hasText(isSuccess)) {

            supply.setBotId(String.valueOf(common.getSpringyBotId()));
            supply.setUserId(String.valueOf(message.getChatId()));
            supply.setLastMessageId(message.getMessageId());

            // 處理資料表
            Long id = common.getSpringyBotId();
            String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
            List<DemandUser> demandUsers = springyBotServiceImpl.findDemandUserBySpringyBotId(id);
            DemandUser demandUser = demandUsers.stream().filter(j -> j.getUserId().equals(userId)).findAny().get();

            final Long final_jobSeekerId = demandUser.getId();

            if (!demandUser.getSupply().stream().anyMatch(p -> p.getId().equals(final_jobSeekerId))) {
                demandUser.getSupply().add(supply);
            }
            demandManagementServiceImpl.saveSupply(supply);

            String result = supplyDTO.generateSupplyDetails(supply);

            List<RobotChannelManagement> robotChannelManagements = springyBotServiceImpl
                    .findRobotChannelManagementBySpringyBotId(common.getSpringyBotId());
            Iterator<RobotChannelManagement> iterator_channel = robotChannelManagements.iterator();

            while (iterator_channel.hasNext()) {
                RobotChannelManagement robotChannelManagement = iterator_channel.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long channelId = robotChannelManagement.getChannelId();
                    String channelTitle = robotChannelManagement.getChannelTitle();
                    String channelLink = robotChannelManagement.getLink();
                    response.setChatId(String.valueOf(channelId));
                    response.setText(DemandEnum.send_supply_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    ChannelMessageIdPostCounts channelMessageIdPostCounts = demandManagementServiceImpl
                            .findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                                    channelId, String.valueOf(message.getChatId()), "supply");

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText(
                                DemandEnum.send_supply_text(result));
                        editMessageText.setChatId(String.valueOf(channelId));
                        editMessageText.setText(DemandEnum.send_supply_text(result));
                        editMessageText.setMessageId(channelMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(supply.getUserId());
                        response.setText("[ " + channelTitle + " ]编辑成功");
                        common.executeAsync(response);
                    } else {
                        if (channelMessageIdPostCounts == null) {

                            final Integer channelMessageId = common.executeAsync(response);
                            response.setChatId(supply.getUserId());
                            response.setText("[ " + channelTitle + " ]发送 [供应] 成功");
                            common.executeAsync(response);

                            channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                            channelMessageIdPostCounts.setBotId(supply.getBotId());
                            channelMessageIdPostCounts.setUserId(supply.getUserId());
                            channelMessageIdPostCounts.setChannelId(channelId);
                            channelMessageIdPostCounts.setChannelTitle(channelTitle);
                            channelMessageIdPostCounts.setChannelLink(channelLink);
                            channelMessageIdPostCounts.setMessageId(channelMessageId);
                            channelMessageIdPostCounts.setPostCount(1);
                            channelMessageIdPostCounts.setType(DemandEnum.SUPPLY_.getText());

                            supply = demandManagementServiceImpl.findByUserIdAndBotIdWithSupply(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

                            supply.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                            demandManagementServiceImpl.saveSupply(supply);
                        } else {
                            if (channelMessageIdPostCounts.getPostCount() == 0) {

                                final Integer channelMessageId = common.executeAsync(response);
                                response.setChatId(supply.getUserId());
                                response.setText("[ " + channelTitle + " ]发送 [供应] 成功");
                                common.executeAsync(response);

                                channelMessageIdPostCounts.setMessageId(channelMessageId);
                                channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                                demandManagementServiceImpl.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                            } else {
                                response.setChatId(supply.getUserId());
                                response.setText("您已在[ " + channelTitle + " ]發送一條 [供应] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                }
            }

            List<RobotGroupManagement> robotGroupManagements = springyBotServiceImpl
                    .findRobotGroupManagementBySpringyBotId(common.getSpringyBotId());
            Iterator<RobotGroupManagement> iterator_group = robotGroupManagements.iterator();

            while (iterator_group.hasNext()) {
                RobotGroupManagement robotGroupManagement = iterator_group.next();
                if (!result.isEmpty()) {
                    SendMessage response = new SendMessage();
                    Long groupId = robotGroupManagement.getGroupId();
                    String groupTitle = robotGroupManagement.getGroupTitle();
                    String groupLink = robotGroupManagement.getLink();
                    response.setChatId(String.valueOf(groupId));
                    response.setText(DemandEnum.send_supply_text(result));
                    response.setReplyMarkup(new TelentButton().keyboardJobMarkup());
                    response.setDisableNotification(true);
                    response.setDisableWebPagePreview(true);
                    GroupMessageIdPostCounts groupMessageIdPostCounts = demandManagementServiceImpl
                            .findByGroupIdAndTypeWithGroupMessageIdPostCounts(
                                    groupId, DemandEnum.SUPPLY_.getText());

                    if (isEdit) {
                        EditMessageText editMessageText = new EditMessageText();
                        editMessageText.setChatId(String.valueOf(groupId));
                        editMessageText.setText(DemandEnum.send_supply_text(result));
                        editMessageText.setMessageId(groupMessageIdPostCounts.getMessageId());
                        editMessageText.setDisableWebPagePreview(true);
                        common.executeAsync(editMessageText);

                        response.setChatId(supply.getUserId());
                        response.setText("[ " + groupTitle + " ]编辑成功");
                        common.executeAsync(response);

                    } else {
                        if (groupMessageIdPostCounts == null) {
                            final Integer groupMessageId = common.executeAsync(response);
                            response.setChatId(supply.getUserId());
                            response.setText("[ " + groupTitle + " ]发送 [供应] 成功");
                            common.executeAsync(response);

                            groupMessageIdPostCounts = new GroupMessageIdPostCounts();
                            groupMessageIdPostCounts.setBotId(supply.getBotId());
                            groupMessageIdPostCounts.setUserId(supply.getUserId());
                            groupMessageIdPostCounts.setGroupId(groupId);
                            groupMessageIdPostCounts.setGroupTitle(groupTitle);
                            groupMessageIdPostCounts.setGroupLink(groupLink);
                            groupMessageIdPostCounts.setMessageId(groupMessageId);
                            groupMessageIdPostCounts.setPostCount(1);
                            groupMessageIdPostCounts.setType(DemandEnum.SUPPLY_.getText());

                            supply = demandManagementServiceImpl.findByUserIdAndBotIdWithSupply(
                                    String.valueOf(message.getChatId()), String.valueOf(common.getSpringyBotId()));

                            supply.getGroupMessageIdPostCounts().add(groupMessageIdPostCounts);
                            demandManagementServiceImpl.saveSupply(supply);
                        } else {
                            if (groupMessageIdPostCounts.getPostCount() == 0) {

                                final Integer groupMessageId = common.executeAsync(response);
                                response.setChatId(supply.getUserId());
                                response.setText("[ " + groupTitle + " ]发送 [供应] 成功");
                                common.executeAsync(response);

                                groupMessageIdPostCounts.setMessageId(groupMessageId);
                                groupMessageIdPostCounts.setPostCount(groupMessageIdPostCounts.getPostCount() + 1);
                                demandManagementServiceImpl.saveGroupMessageIdPostCounts(groupMessageIdPostCounts);
                            } else {
                                response.setChatId(supply.getUserId());
                                response.setText("您已在[ " + groupTitle + " ]發送一條 [供应] 信息");
                                common.executeAsync(response);
                            }
                        }
                    }
                }
            }
        } else {
            SendMessage response = new SendMessage();
            response.setChatId(supply.getUserId());
            response.setText(isSuccess);
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }

    }

    public void setResponse_edit_demand_management(Common common) {

        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        SendMessage response = new SendMessage();
        response.setChatId(userId);
        response.setDisableNotification(true);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String.valueOf(id),
                        userId, DemandEnum.DEMAND_.getText());

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 需求 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String.valueOf(id),
                        userId, DemandEnum.DEMAND_.getText());

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
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            Demand demand = demandManagementServiceImpl.findByUserIdAndBotIdWithDemand(userId,
                    String.valueOf(id));

            SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
            List<DemandUser> demandUsers = springyBotServiceImpl.findDemandUserBySpringyBotId(id);
            DemandUser demandUser = demandUsers.stream().filter(j -> j.getUserId().equals(userId)).findAny().get();

            DemandDTO demandDTO1 = new DemandDTO(userId, String.valueOf(id));

            if (demand != null) {

                response.setText(demandDTO.generateDemandResponse(demand, true));
                demandDTO1 = demandDTO.createDemandDTO(userId, String.valueOf(id));

                response.setReplyMarkup(new DemandButton().keyboard_demand(demandDTO1, true));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                Integer messageId = common.executeAsync(response);
                demand.setLastMessageId(messageId);
                demandUser.getDemand().add(demand);
                demandManagementServiceImpl.saveDemand(demand);
            } else {
                response.setText(DemandEnum.DEMAND_EDITOR_DEFAULT_FORM.getText());
                response.setReplyMarkup(new DemandButton().keyboard_demand(demandDTO1, false));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);

                Demand d = new Demand(userId, String.valueOf(id),
                        common.executeAsync(response));
                demandUser.getDemand().add(d);
                demandManagementServiceImpl.saveDemand(d);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText(DemandEnum.UNPUBLISHED_DEMAND.getText());
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

        }

    }

    public void setResponse_edit_supply_management(Common common) {

        Long id = common.getSpringyBotId();
        String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(common.getUpdate().getMessage().getChatId()));
        response.setDisableNotification(true);
        response.setDisableWebPagePreview(true);

        List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String.valueOf(id),
                        userId, DemandEnum.SUPPLY_.getText());

        List<String> alertMessages_channel = channelMessageIdPostCounts.stream().map(cmpc -> {
            String markdown = "频道 [ " + cmpc.getChannelTitle() + " ] ";
            if (cmpc.getPostCount() <= 0) {
                return "";
            }
            return markdown + " 发布 " + cmpc.getPostCount() + " 则 [ 供应 ] 信息 \n";
        }).filter(str -> !Objects.equals(str, "")).collect(Collectors.toList());

        List<GroupMessageIdPostCounts> groupMessageIdPostCounts = demandManagementServiceImpl
                .findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String.valueOf(id),
                        userId, DemandEnum.SUPPLY_.getText());

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
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);

            Supply supply = demandManagementServiceImpl.findByUserIdAndBotIdWithSupply(userId,
                    String.valueOf(id));

            SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
            List<DemandUser> demandUsers = springyBotServiceImpl.findDemandUserBySpringyBotId(id);

            DemandUser demandUser = demandUsers.stream().filter(j -> j.getUserId().equals(userId)).findAny().get();

            SupplyDTO supplyDTO1 = new SupplyDTO(userId, String.valueOf(id));

            if (supply != null) {

                response.setText(supplyDTO.generateSupplyResponse(supply, true));

                supplyDTO = supplyDTO.createSupplyDTO(userId, String.valueOf(id));

                response.setReplyMarkup(new DemandButton().keyboard_supply(supplyDTO1, true));
                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                Integer messageId = common.executeAsync(response);
                supply.setLastMessageId(messageId);
                demandUser.getSupply().add(supply);
                demandManagementServiceImpl.saveSupply(supply);

            } else {
                response.setText(DemandEnum.SUPPLY_EDITOR_DEFAULT_FORM.getText());
                response.setReplyMarkup(new DemandButton().keyboard_supply(supplyDTO1, true));

                response.setDisableNotification(true);
                response.setDisableWebPagePreview(true);
                Supply s = new Supply(userId, String.valueOf(id),
                        common.executeAsync(response));
                demandUser.getSupply().add(s);
                demandManagementServiceImpl.saveSupply(s);
                springyBotServiceImpl.save(springyBot);
            }

        } else {
            response.setText(DemandEnum.UNPUBLISHED_SUPPLY.getText());
            response.setDisableNotification(true);
            response.setDisableWebPagePreview(true);
            common.executeAsync(response);
        }
    }

}
