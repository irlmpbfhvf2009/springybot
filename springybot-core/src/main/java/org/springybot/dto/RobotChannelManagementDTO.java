package org.springybot.dto;

import lombok.Data;

@Data
public class RobotChannelManagementDTO {

    private Long id;

    private Long botId;

    private Long inviteId;

    private String inviteUsername;

    private String inviteFirstname;

    private String inviteLastname;

    private Long channelId;

    private String channelTitle;

    private String link;

    private Boolean status;
    
}
