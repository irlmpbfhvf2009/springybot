package com.lwdevelop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lwdevelop.bot.talent.utils.Common;
import com.lwdevelop.dto.GroupAndChannelTreeDTO;
import com.lwdevelop.dto.RobotChannelManagementDTO;
import com.lwdevelop.dto.RobotGroupManagementDTO;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.RobotChannelManagementRepository;
import com.lwdevelop.repository.RobotGroupManagementRepository;
import com.lwdevelop.service.RobotGroupAndChannelManagementService;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import com.lwdevelop.utils.ResponseUtils.ResponseData;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Service
public class RobotGroupAndChannelManagementServiceImpl implements RobotGroupAndChannelManagementService {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private RobotGroupManagementRepository robotGroupManagementRepository;

    @Autowired
    private RobotChannelManagementRepository robotChannelManagementRepository;

    @Override
    public void deleteByIdWithRobotChannelManagement(Long Id) {
        robotChannelManagementRepository.deleteById(Id);
    }

    @Override
    public RobotChannelManagement findByBotIdAndChannelId(Long botId, Long channelId) {
        return robotChannelManagementRepository.findByBotIdAndChannelId(botId, channelId);
    }

    @Override
    public void deleteByIdWithRobotGroupManagement(Long Id) {
        robotGroupManagementRepository.deleteById(Id);
    }

    @Override
    public RobotGroupManagement findByBotIdAndGroupId(Long botId, Long groupId) {
        return robotGroupManagementRepository.findByBotIdAndGroupId(botId, groupId);
    }

    @Override
    public ResponseEntity<ResponseData> getGroupAndChannelTreeData() {
        List<GroupAndChannelTreeDTO> data = new ArrayList<>();
        List<SpringyBot> springyBots = springyBotServiceImpl.findAll();

        for (int i = 0; i < springyBots.size(); i++) {
            GroupAndChannelTreeDTO group = new GroupAndChannelTreeDTO();
            group.setLabel("群组");
            group.setId(0L);
            GroupAndChannelTreeDTO channel = new GroupAndChannelTreeDTO();
            channel.setLabel("频道");
            channel.setId(1L);

            for (int j = 0; j < springyBots.get(i).getRobotGroupManagement().size(); j++) {
                List<GroupAndChannelTreeDTO> ff = new ArrayList<>();
                springyBots.get(i).getRobotGroupManagement().stream().forEach(g -> {
                    GroupAndChannelTreeDTO gact = new GroupAndChannelTreeDTO();
                    gact.setId(g.getGroupId());
                    gact.setLabel(g.getGroupTitle());
                    if (g.getStatus()) {
                        RobotGroupManagementDTO robotGroupManagementDTO = new RobotGroupManagementDTO();
                        robotGroupManagementDTO.setBotId(g.getBotId());
                        robotGroupManagementDTO.setGroupId(g.getGroupId());
                        robotGroupManagementDTO.setGroupTitle(g.getGroupTitle());
                        robotGroupManagementDTO.setId(g.getId());
                        robotGroupManagementDTO.setInviteFirstname(g.getInviteFirstname());
                        robotGroupManagementDTO.setInviteId(g.getInviteId());
                        robotGroupManagementDTO.setInviteLastname(g.getInviteLastname());
                        robotGroupManagementDTO.setInviteUsername(g.getInviteUsername());
                        robotGroupManagementDTO.setLink(g.getLink());
                        robotGroupManagementDTO.setStatus(g.getStatus());
                        List<RobotGroupManagementDTO> list = new ArrayList<>();
                        list.add(robotGroupManagementDTO);
                        gact.setRobotGroupManagementDTO(list);
                    }
                    ff.add(gact);
                });
                group.setChildren(ff);
            }

            for (int j = 0; j < springyBots.get(i).getRobotChannelManagement().size(); j++) {
                List<GroupAndChannelTreeDTO> ff = new ArrayList<>();
                springyBots.get(i).getRobotChannelManagement().stream().forEach(c -> {
                    GroupAndChannelTreeDTO cact = new GroupAndChannelTreeDTO();
                    cact.setId(c.getChannelId());
                    cact.setLabel(c.getChannelTitle());
                    if (c.getStatus()) {
                        RobotChannelManagementDTO robotChannelManagementDTO = new RobotChannelManagementDTO();
                        robotChannelManagementDTO.setBotId(c.getBotId());
                        robotChannelManagementDTO.setChannelId(c.getChannelId());
                        robotChannelManagementDTO.setChannelTitle(c.getChannelTitle());
                        robotChannelManagementDTO.setId(c.getId());
                        robotChannelManagementDTO.setInviteFirstname(c.getInviteFirstname());
                        robotChannelManagementDTO.setInviteId(c.getInviteId());
                        robotChannelManagementDTO.setInviteLastname(c.getInviteLastname());
                        robotChannelManagementDTO.setInviteUsername(c.getInviteUsername());
                        robotChannelManagementDTO.setLink(c.getLink());
                        robotChannelManagementDTO.setStatus(c.getStatus());
                        List<RobotChannelManagementDTO> list = new ArrayList<>();
                        list.add(robotChannelManagementDTO);
                        cact.setRobotChannelManagementDTO(list);
                    }
                    ff.add(cact);
                });
                channel.setChildren(ff);
            }

            GroupAndChannelTreeDTO jobTreeDTO = new GroupAndChannelTreeDTO();
            List<GroupAndChannelTreeDTO> children = new ArrayList<>();
            children.add(group);
            children.add(channel);

            jobTreeDTO.setLabel(springyBots.get(i).getUsername());
            jobTreeDTO.setId((long) i);
            jobTreeDTO.setChildren(children);
            data.add(jobTreeDTO);
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public boolean ifSubscribeChannel(Common common) {
        String chatId = "-1001784108917";
        Long userId = common.getUpdate().getMessage().getChatId();
        GetChatMember getChatMember = new GetChatMember(chatId,userId);
        ChatMember c = common.getChatMemberAsync(getChatMember);

        if (!c.getStatus().equals("left")){
            return true;
        }
        return false;
    }

}
