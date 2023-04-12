package com.lwdevelop.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.lwdevelop.dto.GroupAndChannelTreeDTO;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.RobotChannelManagementRepository;
import com.lwdevelop.repository.RobotGroupManagementRepository;
import com.lwdevelop.service.RobotGroupAndChannelManagementService;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import com.lwdevelop.utils.ResponseUtils.ResponseData;

@Service
public class RobotGroupAndChannelManagementServiceImpl implements RobotGroupAndChannelManagementService{

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
    public ResponseEntity<ResponseData> getJobTreeData() {
        List<GroupAndChannelTreeDTO> data = new ArrayList<>();
        List<SpringyBot> springyBots = springyBotServiceImpl.findAll();
    
        // for (int i = 0; i < springyBots.size(); i++) {
        //     GroupAndChannelTreeDTO posting = new GroupAndChannelTreeDTO();
        //     posting.setLabel("招聘信息");
        //     posting.setId(0L);
        //     GroupAndChannelTreeDTO seeker = new GroupAndChannelTreeDTO();
        //     seeker.setLabel("求職信息");
        //     seeker.setId(1L);
        //     for (int j = 0; j < springyBots.get(i).getRobotGroupManagement().size(); j++) {
        //         springyBots.get(i).getRobotGroupManagement().stream().forEach(group -> {
        //             GroupAndChannelTreeDTO gact = new GroupAndChannelTreeDTO();
        //             List<GroupAndChannelTreeDTO> ff = new ArrayList<>();
        //             gact.setId(jobUser.getId());
        //             user.setLabel(jobUser.getUsername());
        //             user.setChildren(null);
        //             ff.add(user);
        //             posting.setChildren(ff);
        //             seeker.setChildren(ff);
        //         });
        //     }
    
            // GroupAndChannelTreeDTO jobTreeDTO = new GroupAndChannelTreeDTO();
            // List<GroupAndChannelTreeDTO> children = new ArrayList<>();
            // children.add(seeker);
            // children.add(posting);
    
            // jobTreeDTO.setLabel(springyBots.get(i).getUsername());
            // jobTreeDTO.setId((long) i);
            // jobTreeDTO.setChildren(children);
            // data.add(jobTreeDTO);
        // }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }


    
}
