package com.glp.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.glp.dto.InvitationBonusUserDTO;
import com.glp.entity.InvitationBonusUser;
import com.glp.utils.ResponseUtils;

public interface InvitationBonusUserService {

    List<InvitationBonusUser> findAllByPage(int page, int pageSize);
    
    ResponseEntity<ResponseUtils.ResponseData> getAllInvitationBonusUser(int page, int pageSize);
    
    ResponseEntity<ResponseUtils.ResponseData> settlement(InvitationBonusUserDTO invitationBonusUserDTO);

}
