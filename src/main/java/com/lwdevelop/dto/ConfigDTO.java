package com.lwdevelop.dto;

import lombok.Data;

@Data
public class ConfigDTO {

    private Boolean inviteFriendsSet;

    private Boolean followChannelSet;
    
    private Long followChannelSet_chatId;

    private String followChannelSet_chatTitle;

    private Boolean invitationBonusSet;

    private int deleteSeconds;

    private int inviteFriendsAutoClearTime;

    private int inviteFriendsQuantity;

    private int inviteMembers;

    private double inviteEarnedOutstand;

    private String contactPerson;
    
    private String password;
}
