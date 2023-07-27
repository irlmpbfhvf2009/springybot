package org.springybot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springybot.service.impl.RobotGroupAndChannelManagementServiceImpl;
import org.springybot.utils.ResponseUtils;

@RestController
@RequestMapping("/groupAndChannelManagement")
public class GroupAndChannelManagement {

    @Autowired
    RobotGroupAndChannelManagementServiceImpl robotGroupAndChannelManagementServiceImpl;

    /**
     * 獲取群組和頻道樹狀結構數據
     * 
     * 獲取群組和頻道的樹狀結構數據。
     * 
     * @return 包含群組和頻道樹狀結構數據的 ResponseEntity 物件
     * @throws Exception 獲取數據過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/getGroupAndChannelTreeData")
    public ResponseEntity<ResponseUtils.ResponseData> getGroupAndChannelTreeData() throws Exception {
        return robotGroupAndChannelManagementServiceImpl.getGroupAndChannelTreeData();
    }

}
