package com.lwdevelop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ApiModel
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SpringyBotDTO {

    @ApiModelProperty(value = "編號", required = true)
    private Long id;
    
    private String token;
    
    private String username;

    private Boolean state;

    private Boolean inviteFriendsSet;
    
    private Boolean followChannelSet;

    private Boolean invitationBonusSet;
    
    private int deleteSeconds;

    private int inviteFriendsAutoClearTime;

    private int inviteFriendsQuantity;

    private int inviteMembers;

    private double inviteEarnedOutstand;

    private String contactPerson;
}
