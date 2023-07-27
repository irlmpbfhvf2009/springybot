package org.springybot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class GroupMessageIdPostCounts {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String botId;

    private String userId;

    private Long groupId;

    private String groupTitle;

    private String groupLink;

    private Integer messageId;

    private Integer postCount;

    private String type; // seeker , posting
}
