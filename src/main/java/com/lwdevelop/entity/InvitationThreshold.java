package com.lwdevelop.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
@Entity
public class InvitationThreshold {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Inviter;

    private String invitedUser;

    private Long groupId;

    private String groupTitle;
    
    @CreatedDate
    private Date createDate;
}