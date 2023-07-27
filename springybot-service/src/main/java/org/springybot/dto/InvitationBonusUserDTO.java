package org.springybot.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class InvitationBonusUserDTO {

    private Long id;

    private Long botId;

    // user
    private Long userId;
    private String firstname;
    private String username;
    private String lastname;

    // chat
    private Long chatId;
    private String chatTitle;

    // 有效獎金(未結金額)
    private BigDecimal outstandingAmount;
    
    // 累積結算獎金(清算後獎金)
    private BigDecimal settlementAmount;

    // 結算前邀請人紀錄(防止重複邀請重複計算)
    private List<String> pendingInvitations;

    // 結算後邀請人紀錄(防止重複邀請重複計算)
    private List<String> accumulatedInvitations;
}
