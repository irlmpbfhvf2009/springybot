package org.springybot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ChannelMessageIdPostCounts {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private String botId;

    private String userId;

    private Long channelId;

    private String channelTitle;

    private String channelLink;

    private Integer messageId;

    private Integer postCount;

    private String type; // seeker , posting

}
