package com.lwdevelop.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    private double inviteEarnedOutstand;

    private String contactPerson;

    private String password;

}
