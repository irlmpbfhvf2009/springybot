package org.springybot.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springybot.dto.InvitationBonusUserDTO;
import org.springybot.entity.InvitationBonusUser;
import org.springybot.utils.ResponseUtils;

public interface InvitationBonusUserService {

    List<InvitationBonusUser> findAllByPage(int page, int pageSize);
    
    ResponseEntity<ResponseUtils.ResponseData> getAllInvitationBonusUser(int page, int pageSize);
    
    ResponseEntity<ResponseUtils.ResponseData> settlement(InvitationBonusUserDTO invitationBonusUserDTO);

}
