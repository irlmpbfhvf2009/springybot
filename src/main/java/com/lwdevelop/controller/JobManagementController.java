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

    /**
     * 編輯並發布職位招聘
     * 
     * 根據提供的職位招聘資料，進行編輯並發布職位招聘。
     * 
     * @param jobPostingDTO 包含職位招聘資訊的 JobPostingDTO 物件，來自請求的 request body
     * @return 包含編輯並發布結果的 ResponseEntity 物件
     * @throws Exception 編輯並發布職位招聘過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/editAndPost_JobPosting")
    public ResponseEntity<ResponseUtils.ResponseData> editAndPost_JobPosting(
            @RequestBody JobPostingDTO jobPostingDTO) throws Exception {

        return jobManagementServiceImpl.editAndPost_JobPosting(jobPostingDTO);
    }

    /**
     * 編輯並發布求職者資訊
     * 
     * 根據提供的求職者資料，進行編輯並發布求職者資訊。
     * 
     * @param jobSeekerDTO 包含求職者資訊的 JobSeekerDTO 物件，來自請求的 request body
     * @return 包含編輯並發布結果的 ResponseEntity 物件
     * @throws Exception 編輯並發布求職者資訊過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/editAndPost_JobSeeker")
    public ResponseEntity<ResponseUtils.ResponseData> editAndPost_JobSeeker(
            @RequestBody JobSeekerDTO jobSeekerDTO) throws Exception {

        return jobManagementServiceImpl.editAndPost_JobSeeker(jobSeekerDTO);
    }

    /**
     * 編輯職位招聘
     * 
     * 根據提供的職位招聘資料，進行編輯職位招聘。
     * 
     * @param jobPostingDTO 包含職位招聘資訊的 JobPostingDTO 物件，來自請求的 request body
     * @return 包含編輯結果的 ResponseEntity 物件
     * @throws Exception 編輯職位招聘過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/edit_JobPosting")
    public ResponseEntity<ResponseUtils.ResponseData> edit_JobPosting(
            @RequestBody JobPostingDTO jobPostingDTO) throws Exception {

        return jobManagementServiceImpl.edit_JobPosting(jobPostingDTO);
    }

    /**
     * 編輯求職者資訊
     * 
     * 根據提供的求職者資料，進行編輯求職者資訊。
     * 
     * @param jobSeekerDTO 包含求職者資訊的 JobSeekerDTO 物件，來自請求的 request body
     * @return 包含編輯結果的 ResponseEntity 物件
     * @throws Exception 編輯求職者資訊過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/edit_JobSeeker")
    public ResponseEntity<ResponseUtils.ResponseData> edit_JobSeeker(
            @RequestBody JobSeekerDTO jobSeekerDTO) throws Exception {

        return jobManagementServiceImpl.edit_JobSeeker(jobSeekerDTO);
    }

    /**
     * 使用職位招聘解密 UB
     * 
     * 根據提供的職位招聘資料，進行解密 UB 的操作。
     * 
     * @param jobPostingDTO 包含職位招聘資訊的 JobPostingDTO 物件，來自請求的 request body
     * @return 包含解密 UB 結果的 ResponseEntity 物件
     * @throws Exception 解密 UB 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/decryptedUbWithJobPosting")
    public ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobPosting(
            @RequestBody JobPostingDTO jobPostingDTO) throws Exception {
        return jobManagementServiceImpl.decryptedUbWithJobPosting(jobPostingDTO);
    }

    /**
     * 使用求職者解密 UB
     * 
     * 根據提供的求職者資料，進行解密 UB 的操作。
     * 
     * @param jobSeekerDTO 包含求職者資訊的 JobSeekerDTO 物件，來自請求的 request body
     * @return 包含解密 UB 結果的 ResponseEntity 物件
     * @throws Exception 解密 UB 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/decryptedUbWithJobSeeker")
    public ResponseEntity<ResponseUtils.ResponseData> decryptedUbWithJobSeeker(
            @RequestBody JobSeekerDTO jobSeekerDTO) throws Exception {
        return jobManagementServiceImpl.decryptedUbWithJobSeeker(jobSeekerDTO);
    }

    /**
     * 獲取職位樹狀數據
     * 
     * 獲取職位的樹狀結構數據。
     * 
     * @return 包含職位樹狀數據的 ResponseEntity 物件
     * @throws Exception 獲取職位樹狀數據過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/getJobTreeData")
    public ResponseEntity<ResponseUtils.ResponseData> getJobTreeData() throws Exception {
        return jobManagementServiceImpl.getJobTreeData();
    }

}
