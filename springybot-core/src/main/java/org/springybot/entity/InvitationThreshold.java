package org.springybot.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class InvitationThreshold {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long botId;

    private Long inviteId;

    private String inviteFirstname;

    private String inviteUsername;

    private String inviteLastname;

    private Boolean inviteStatus;

    private Long invitedId;

    private String invitedFirstname;

    private String invitedUsername;

    private String invitedLastname;

    private Boolean invitedStatus;

    private Long chatId;

    private String chatTitle;

    private String type; // channel or group

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}
