package com.lwdevelop.dto;

import java.util.List;
import lombok.Data;

@Data
public class GroupAndChannelTreeDTO {
    Long id;
    String label;
    List<GroupAndChannelTreeDTO> children;

    List<RobotGroupManagementDTO> robotGroupManagementDTO;
    List<RobotChannelManagementDTO> robotChannelManagementDTO;
}
