package com.lwdevelop.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean inviteFriendsSet;
    
    private Boolean followChannelSet;

    private Boolean invitationBonusSet;
    
    private int deleteSeconds;

    private int inviteFriendsAutoClearTime;

    private int inviteFriendsQuantity;

    private int inviteMembers;

    private double inviteEarnedOutstand;

    private String contactPerson;

    @OneToOne(cascade = CascadeType.ALL)
    private SpringyBot springyBot;
}
