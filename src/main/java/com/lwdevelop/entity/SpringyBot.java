package com.lwdevelop.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SpringyBot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;
    
    private String username;

    private Boolean state;

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
