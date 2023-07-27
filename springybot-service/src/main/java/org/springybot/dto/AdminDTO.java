package org.springybot.dto;

import java.util.List;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
// @ApiModel
@ToString
public class AdminDTO {
    
    // @ApiModelProperty(value = "編號", required = true)
    private Long id;
    
    // @ApiModelProperty(value = "用戶名", required = true)
    private String username;

    // @ApiModelProperty(value = "密碼", required = true)
    private String password;

    // @ApiModelProperty(value = "狀態", required = true)
    private Boolean enabled;

    // @ApiModelProperty(value = "角色", required = true)
    private List<String> roles;
    
    
}
