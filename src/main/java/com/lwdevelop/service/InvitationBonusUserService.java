package com.lwdevelop.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.dto.InvitationBonusUserDTO;
import com.lwdevelop.entity.InvitationBonusUser;
import com.lwdevelop.utils.ResponseUtils;

public interface InvitationBonusUserService {

    List<InvitationBonusUser> findAllByPage(int page, int pageSize);
    
    ResponseEntity<ResponseUtils.ResponseData> getAllInvitationBonusUser(int page, int pageSize);
    
    ResponseEntity<ResponseUtils.ResponseData> settlement(InvitationBonusUserDTO invitationBonusUserDTO);

}
