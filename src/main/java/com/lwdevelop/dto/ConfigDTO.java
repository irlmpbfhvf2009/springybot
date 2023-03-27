package com.lwdevelop.dto;

import lombok.Data;

@Data
public class ConfigDTO {

    private Boolean inviteFriendsSet;

    private Boolean followChannelSet;

    private Boolean invitationBonusSet;

    private int deleteSeconds;

    private int inviteFriendsAutoClearTime;

    private int inviteFriendsQuantity;

    private int inviteMembers;

    private double inviteEarnedOutstand;

    private String contactPerson;

}
