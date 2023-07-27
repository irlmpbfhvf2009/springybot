package org.springybot.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ConfigDTO {

    private Long id;
    
    private Boolean inviteFriendsSet;

    private Boolean followChannelSet;
    
    private Long followChannelSet_chatId;

    private String followChannelSet_chatTitle;

    private Boolean invitationBonusSet;

    private int deleteSeconds;

    private int inviteFriendsAutoClearTime;

    private int inviteFriendsQuantity;

    private int inviteMembers;

    private BigDecimal inviteEarnedOutstand;

    private BigDecimal minimumPayout;

    private String contactPerson;
    
    private String password;
}
