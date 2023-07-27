package org.springybot.dto;

import lombok.Data;

@Data
public class RobotGroupManagementDTO {

    private Long id;

    private Long botId;

    private Long inviteId;

    private String inviteUsername;

    private String inviteFirstname;

    private String inviteLastname;

    private Long groupId;

    private String groupTitle;

    private String link;

    private Boolean status;

}
