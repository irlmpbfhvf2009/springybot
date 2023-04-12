package com.lwdevelop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lwdevelop.service.impl.RobotGroupAndChannelManagementServiceImpl;
import com.lwdevelop.utils.ResponseUtils;

@RestController
@RequestMapping("/groupAndChannelManagement")
public class GroupAndChannelManagement {

    @Autowired
    RobotGroupAndChannelManagementServiceImpl robotGroupAndChannelManagementServiceImpl;

    // 樹聯動
    @PostMapping("/getJobTreeData")
    public ResponseEntity<ResponseUtils.ResponseData> getJobTreeData() throws Exception {
        return robotGroupAndChannelManagementServiceImpl.getJobTreeData();
    }

}
