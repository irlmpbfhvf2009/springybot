package org.springybot.entity;

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

    private Long botId;

    private Long inviteId;

    private String inviteUsername;

    private String inviteFirstname;

    private String inviteLastname;

    private Long groupId;

    private String groupTitle;

    private String link;

    private String type; // group or supergroup

    private Boolean status;
    
}
