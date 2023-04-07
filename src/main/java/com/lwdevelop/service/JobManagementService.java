package com.lwdevelop.service;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.utils.ResponseUtils;

public interface JobManagementService {
    // CRUD
    JobSeeker findByUserIdWithJobSeeker(String userId,String botId);

    JobPosting findByUserIdWithJobPosting(String userId,String botId);

    void deleteByIdWithJobPosting(Long id);
    void deleteByUserIdWithJobPosting(String userId);

    void saveJobPosting(JobPosting jobPosting);
    void saveJobSeeker(JobSeeker jobSeeker);


    //

    ResponseEntity<ResponseUtils.ResponseData> addJobPosting(JobPostingDTO jobPostingDTO);

    ResponseEntity<ResponseUtils.ResponseData> addJobSeeker(JobSeekerDTO jobSeekerDTO);
    
    ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobPosting(JobPostingDTO jobPostingDTO);

    ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobSeeker(JobSeekerDTO jobSeekerDTO);

    ResponseEntity<ResponseUtils.ResponseData> getJobTreeData();


}
