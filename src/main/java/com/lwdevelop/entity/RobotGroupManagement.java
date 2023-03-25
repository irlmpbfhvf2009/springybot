package com.lwdevelop.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class RobotGroupManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long fromBotId;

    private Long inviteId;

    private String inviteUsername;

    private String inviteFirstname;

    private Long groupId;

    private String groupTitle;

    private String link;


}
