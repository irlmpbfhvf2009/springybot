package com.lwdevelop.service;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.utils.ResponseUtils;

public interface JobManagementService {

    ResponseEntity<ResponseUtils.ResponseData> addJobPosting(JobPostingDTO jobPostingDTO);

    ResponseEntity<ResponseUtils.ResponseData> addJobSeeker(JobSeekerDTO jobSeekerDTO);
    
}
