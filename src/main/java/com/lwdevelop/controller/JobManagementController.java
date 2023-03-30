package com.lwdevelop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PostMapping("/addJobPosting")
    public ResponseEntity<ResponseUtils.ResponseData> addJobPosting(
            @RequestBody JobPostingDTO jobPostingDTO) throws Exception {

        return jobManagementServiceImpl.addJobPosting(jobPostingDTO);
    }

    // 新增求職信息
    @PostMapping("/addJobSeeker")
    public ResponseEntity<ResponseUtils.ResponseData> addJobSeeker(
            @RequestBody JobSeekerDTO jobSeekerDTO) throws Exception {

        return jobManagementServiceImpl.addJobSeeker(jobSeekerDTO);
    }

    // 解碼招聘信息
    @PostMapping("/decryptedUbWithJobPosting")
    public ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobPosting(
            @RequestParam String ub) throws Exception {

        return jobManagementServiceImpl.decryptedUbWithJobPosting(ub);
    }

}
