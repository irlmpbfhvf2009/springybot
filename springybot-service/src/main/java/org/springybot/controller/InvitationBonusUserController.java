package org.springybot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springybot.dto.InvitationBonusUserDTO;
import org.springybot.service.impl.InvitationBonusUserServiceImpl;
import org.springybot.utils.ResponseUtils;

@RestController
@RequestMapping("/invitationBonusUser")
public class InvitationBonusUserController {
    
    @Autowired
    private InvitationBonusUserServiceImpl invitationBonusUserServiceImpl;

     /**
     * 獲取所有 SpringyBot
     * 
     * 獲取所有已存在的 SpringyBot。
     * 
     * @param page     頁數，默認值為 0，從第一頁開始
     * @param pageSize 每頁數量，默認值為 10
     * @return 包含 SpringyBot 列表的 ResponseEntity 物件
     * @throws Exception 獲取 SpringyBot 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 20, 2023
     * @author Leo
     */
    @PostMapping("/v1/getAllInvitationBonusUser")
    public ResponseEntity<ResponseUtils.ResponseData> getAllBot(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {

        return invitationBonusUserServiceImpl.getAllInvitationBonusUser(page, pageSize);
    }

    @PostMapping("/v1/settlement")
    public ResponseEntity<ResponseUtils.ResponseData> settlement(@RequestBody InvitationBonusUserDTO invitationBonusUserDTO){
        return invitationBonusUserServiceImpl.settlement(invitationBonusUserDTO);
    }
}
