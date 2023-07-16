package com.lwdevelop.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.utils.ResponseUtils;

public interface JobManagementService {
    // CRUD
    JobSeeker findByUserIdAndBotIdWithJobSeeker(String userId,String springyBotId);

    JobPosting findByUserIdAndBotIdWithJobPosting(String userId,String springyBotId);

    void deleteByIdWithJobPosting(Long id);
    void deleteByUserIdWithJobPosting(String userId);

    void saveJobPosting(JobPosting jobPosting);
    void saveJobSeeker(JobSeeker jobSeeker);
    
    // ChannelMessageIdPostCounts
    ChannelMessageIdPostCounts findByChannelIdAndTypeWithChannelMessageIdPostCounts(Long channelId,String type);
    List<ChannelMessageIdPostCounts> findAllByChannelIdAndTypeWithChannelMessageIdPostCounts(Long channelId,String type);
    ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,String userId,String type);
    ChannelMessageIdPostCounts findByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,String userId,String type);
    List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,String userId,String type);
    void deleteByIdChannelMessageIdPostCounts(Long id);
    void saveChannelMessageIdPostCounts(ChannelMessageIdPostCounts channelMessageIdPostCounts);
    
    List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String botId,String userId,String type);
    GroupMessageIdPostCounts findByGroupIdAndTypeWithGroupMessageIdPostCounts(Long groupId,String type);
    List<GroupMessageIdPostCounts> findAllByGroupIdAndTypeWithGroupMessageIdPostCounts(Long groupId,String type);
    void deleteByIdGroupMessageIdPostCounts(Long id);
    void saveGroupMessageIdPostCounts(GroupMessageIdPostCounts groupMessageIdPostCounts);


    //
    ResponseEntity<ResponseUtils.ResponseData> editAndPost_JobPosting(JobPostingDTO jobPostingDTO);

    ResponseEntity<ResponseUtils.ResponseData> editAndPost_JobSeeker(JobSeekerDTO jobSeekerDTO);

    ResponseEntity<ResponseUtils.ResponseData> edit_JobPosting(JobPostingDTO jobPostingDTO);

    ResponseEntity<ResponseUtils.ResponseData> edit_JobSeeker(JobSeekerDTO jobSeekerDTO);
    
    ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobPosting(JobPostingDTO jobPostingDTO);

    ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobSeeker(JobSeekerDTO jobSeekerDTO);

    ResponseEntity<ResponseUtils.ResponseData> getJobTreeData();




}
