package com.lwdevelop.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.utils.ResponseUtils;

public interface JobManagementService {
    // CRUD
    JobSeeker findByUserIdAndBotIdWithJobSeeker(String userId,String springyBotId);

    JobPosting findByUserIdAndBotIdWithJobPosting(String userId,String springyBotId);

    void saveJobUser(JobUser jobUser);
    void saveJobSeeker(JobSeeker jobSeeker);
    
    // JobPosting
    void saveJobPosting(JobPosting jobPosting);
    
    // ChannelMessageIdPostCounts
    GroupMessageIdPostCounts findByGroupIdAndUserIdAndTypeWithGroupMessageIdPostCounts(Long groupId,String userId,String type);
    ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,String userId,String type);

    List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,String userId,String type);
    List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String botId,String userId,String type);
    
    void saveChannelMessageIdPostCounts(ChannelMessageIdPostCounts channelMessageIdPostCounts);
    void saveGroupMessageIdPostCounts(GroupMessageIdPostCounts groupMessageIdPostCounts);

    ResponseEntity<ResponseUtils.ResponseData> getJobTreeData();

}
