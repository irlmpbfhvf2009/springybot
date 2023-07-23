package com.lwdevelop.service;

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
    // ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,String userId,String type);
    // ChannelMessageIdPostCounts findByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,String userId,String type);
    List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,String userId,String type);
    // void deleteByIdChannelMessageIdPostCounts(Long id);
    void saveChannelMessageIdPostCounts(ChannelMessageIdPostCounts channelMessageIdPostCounts);
    
    List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String botId,String userId,String type);
    GroupMessageIdPostCounts findByGroupIdAndUserIdAndTypeWithGroupMessageIdPostCounts(Long groupId,String userId,String type);
    ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithGroupMessageIdPostCounts(Long channel,String userId,String type);
    void saveGroupMessageIdPostCounts(GroupMessageIdPostCounts groupMessageIdPostCounts);

    ResponseEntity<ResponseUtils.ResponseData> getDemandTreeData();

}
