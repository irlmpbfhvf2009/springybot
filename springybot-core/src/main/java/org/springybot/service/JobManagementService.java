package org.springybot.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springybot.entity.ChannelMessageIdPostCounts;
import org.springybot.entity.GroupMessageIdPostCounts;
import org.springybot.entity.JobPosting;
import org.springybot.entity.JobSeeker;
import org.springybot.entity.TgUser;
import org.springybot.utils.ResponseUtils;

public interface JobManagementService {

    JobSeeker findByUserIdAndBotIdWithJobSeeker(String userId, String springyBotId);

    JobPosting findByUserIdAndBotIdWithJobPosting(String userId, String springyBotId);

    GroupMessageIdPostCounts findByGroupIdAndUserIdAndTypeWithGroupMessageIdPostCounts(Long groupId, String userId,
            String type);

    ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,
            String userId, String type);

    void saveTgUser(TgUser tgUser);

    void saveJobSeeker(JobSeeker jobSeeker);

    void saveJobPosting(JobPosting jobPosting);

    void saveChannelMessageIdPostCounts(ChannelMessageIdPostCounts channelMessageIdPostCounts);

    void saveGroupMessageIdPostCounts(GroupMessageIdPostCounts groupMessageIdPostCounts);

    List<JobSeeker> findAllByUserIdAndBotIdWithJobSeeker(String userId, String springyBotId);

    List<JobPosting> findAllByUserIdAndBotIdWithJobPosting(String userId, String springyBotId);

    List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,
            String userId, String type);

    List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String botId,
            String userId, String type);

    ResponseEntity<ResponseUtils.ResponseData> getJobTreeData();

}
