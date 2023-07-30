package org.springybot.dto;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
// @ApiModel
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SpringyBotDTO {

    // @ApiModelProperty(value = "編號", required = true)
    private Long id;
    
    private String token;
    
    private String username;

    private Boolean state;

    private String botModel;

    private String botType;

    private String botPath;
    
}
