package org.springybot.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springybot.dto.InvitationBonusUserDTO;
import org.springybot.entity.InvitationBonusUser;
import org.springybot.repository.InvitationBonusUserRepository;
import org.springybot.service.InvitationBonusUserService;
import org.springybot.utils.CommUtils;
import org.springybot.utils.ResponseUtils;
import org.springybot.utils.ResponseUtils.ResponseData;
import org.springybot.utils.RetEnum;

// @Slf4j
@Service
public class InvitationBonusUserServiceImpl implements InvitationBonusUserService {

    @Autowired
    private InvitationBonusUserRepository invitationBonusUserRepository;

    @Override
    public List<InvitationBonusUser> findAllByPage(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return invitationBonusUserRepository.findAll(pageable).getContent();
    }

    @Override
    public ResponseEntity<ResponseData> getAllInvitationBonusUser(int page, int pageSize) {
        HashMap<Object, Object> data = new HashMap<>();
        List<InvitationBonusUser> invitationBonusUserAllList = findAllByPage(page, pageSize);

        Object pager = CommUtils.Pager(page, pageSize, invitationBonusUserAllList.size());
        data.put("list", invitationBonusUserAllList);
        data.put("pager", pager);

        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> settlement(InvitationBonusUserDTO invitationBonusUserDTO) {

        Long id = invitationBonusUserDTO.getId();
        InvitationBonusUser invitationBonusUser = invitationBonusUserRepository.findById(id).get();
        invitationBonusUser.setOutstandingAmount(BigDecimal.valueOf(0));
        invitationBonusUser.setPendingInvitations(new ArrayList<>());
        invitationBonusUser.setAccumulatedInvitations(invitationBonusUserDTO.getPendingInvitations());
        invitationBonusUser.setSettlementAmount(invitationBonusUserDTO.getOutstandingAmount());
        invitationBonusUserRepository.save(invitationBonusUser);

        return ResponseUtils.response(RetEnum.RET_SUCCESS, "用户结算成功");
    }

}
