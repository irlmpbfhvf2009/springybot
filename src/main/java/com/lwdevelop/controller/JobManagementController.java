package com.lwdevelop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.utils.ResponseUtils;

@RestController
@RequestMapping("/jobManagement")
public class JobManagementController {
    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl;
    
    // 新增招聘信息
    @PostMapping("/editAndPost_JobPosting")
    public ResponseEntity<ResponseUtils.ResponseData> editAndPost_JobPosting(
            @RequestBody JobPostingDTO jobPostingDTO) throws Exception {

        return jobManagementServiceImpl.editAndPost_JobPosting(jobPostingDTO);
    }

    // 新增求職信息
    @PostMapping("/editAndPost_JobSeeker")
    public ResponseEntity<ResponseUtils.ResponseData> editAndPost_JobSeeker(
            @RequestBody JobSeekerDTO jobSeekerDTO) throws Exception {

        return jobManagementServiceImpl.editAndPost_JobSeeker(jobSeekerDTO);
    }

    // 解碼招聘信息
    @PostMapping("/decryptedUbWithJobPosting")
    public ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobPosting(
            @RequestBody JobPostingDTO jobPostingDTO) throws Exception {
        return jobManagementServiceImpl.decryptedUbWithJobPosting(jobPostingDTO);
    }

    // 解碼求職信息
    @PostMapping("/decryptedUbWithJobSeeker")
    public ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobSeeker(
            @RequestBody JobSeekerDTO jobSeekerDTO) throws Exception {
        return jobManagementServiceImpl.decryptedUbWithJobSeeker(jobSeekerDTO);
    }

    // 樹聯動
    @PostMapping("/getJobTreeData")
    public ResponseEntity<ResponseUtils.ResponseData> getJobTreeData() throws Exception {
        return jobManagementServiceImpl.getJobTreeData();
    }

}
