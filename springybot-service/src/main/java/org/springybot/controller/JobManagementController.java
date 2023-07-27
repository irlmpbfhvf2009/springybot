package org.springybot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springybot.service.impl.JobManagementServiceImpl;
import org.springybot.utils.ResponseUtils;

@RestController
@RequestMapping("/jobManagement")
public class JobManagementController {
    @Autowired
    private JobManagementServiceImpl jobManagementServiceImpl;

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
