package com.lwdevelop.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String botType;

    private Long botId;

    @OneToOne(cascade = CascadeType.ALL)
    private Config config;

    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<WhiteList> whiteList;

    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RobotGroupManagement> robotGroupManagement;

    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RobotChannelManagement> robotChannelManagement;
    
    // 群組發言(邀請人數)門檻
    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<InvitationThreshold> invitationThreshold;

    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private  Set<JobUser> jobUser;

    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private  Set<RestrictMember> restrictMember;

    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private  Set<RecordChannelUsers> recordChannelUsers;

    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private  Set<RecordGroupUsers> recordGroupUsers;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}
