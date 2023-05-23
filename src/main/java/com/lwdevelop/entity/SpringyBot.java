package com.lwdevelop.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class SpringyBot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private String username;

    private Boolean state;

    private String botType;

    @OneToOne(cascade = CascadeType.ALL)
    private Config config;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<WhiteList> whiteList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RobotGroupManagement> robotGroupManagement;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RobotChannelManagement> robotChannelManagement;
    
    // 群組發言(邀請人數)門檻
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<InvitationThreshold> invitationThreshold;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private  Set<JobUser> jobUser;

}
