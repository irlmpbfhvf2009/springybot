package com.lwdevelop.service;

import com.lwdevelop.dto.DemandDTO;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.SupplyDTO;
import com.lwdevelop.entity.*;
import com.lwdevelop.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DemandManagementService {
    // CRUD
    Supply findByUserIdAndBotIdWithSupply(String userId, String springyBotId);

    Demand findByUserIdAndBotIdWithDemand(String userId,String springyBotId);

    void deleteByIdWithDemand(Long id);

    void saveDemand(Demand demand);
    void saveSupply(Supply supply);
    
    // ChannelMessageIdPostCounts
    ChannelMessageIdPostCounts findByChannelIdAndTypeWithChannelMessageIdPostCounts(Long channelId,String type);
    ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,String userId,String type);
    ChannelMessageIdPostCounts findByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,String userId,String type);
    List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,String userId,String type);
    void deleteByIdChannelMessageIdPostCounts(Long id);
    void saveChannelMessageIdPostCounts(ChannelMessageIdPostCounts channelMessageIdPostCounts);
    
    List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String botId,String userId,String type);
    GroupMessageIdPostCounts findByGroupIdAndTypeWithGroupMessageIdPostCounts(Long groupId,String type);
    void deleteByIdGroupMessageIdPostCounts(Long id);
    void saveGroupMessageIdPostCounts(GroupMessageIdPostCounts groupMessageIdPostCounts);

    ResponseEntity<ResponseUtils.ResponseData> getDemandTreeData();

}
