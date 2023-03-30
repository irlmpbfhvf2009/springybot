package com.lwdevelop.service;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.utils.ResponseUtils;

public interface JobManagementService {
    // CRUD
    JobSeeker findByUserIdWithJobSeeker(String userId);

    JobPosting findByUserIdWithJobPosting(String userId);

    ResponseEntity<ResponseUtils.ResponseData> addJobPosting(JobPostingDTO jobPostingDTO);

    ResponseEntity<ResponseUtils.ResponseData> addJobSeeker(JobSeekerDTO jobSeekerDTO);
    
    ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobPosting(JobPostingDTO jobPostingDTO);
}
