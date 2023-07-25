package com.lwdevelop.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.TgUser;
import com.lwdevelop.utils.ResponseUtils;

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
