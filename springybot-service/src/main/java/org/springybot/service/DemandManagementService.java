package org.springybot.service;

import org.springybot.entity.*;
import org.springybot.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface DemandManagementService {

    Supply findByUserIdAndBotIdWithSupply(String userId, String springyBotId);

    Demand findByUserIdAndBotIdWithDemand(String userId, String springyBotId);

    GroupMessageIdPostCounts findByGroupIdAndUserIdAndTypeWithGroupMessageIdPostCounts(Long groupId, String userId,
            String type);

    ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,
            String userId, String type);

    void saveTgUser(TgUser tgUser);

    void saveDemand(Demand demand);

    void saveSupply(Supply supply);

    void saveChannelMessageIdPostCounts(ChannelMessageIdPostCounts channelMessageIdPostCounts);

    void saveGroupMessageIdPostCounts(GroupMessageIdPostCounts groupMessageIdPostCounts);

    List<Demand> findAllByUserIdAndBotIdWithDemand(String userId, String springyBotId);

    List<Supply> findAllByUserIdAndBotIdWithSupply(String userId, String springyBotId);

    List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,
            String userId, String type);

    List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String botId,
            String userId, String type);

    ResponseEntity<ResponseUtils.ResponseData> getDemandTreeData();

}
