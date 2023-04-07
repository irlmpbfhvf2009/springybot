package com.lwdevelop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ApiModel
@ToString
public class AdvertiseDTO {


    @ApiModelProperty(value = "編號", required = true)
    private Long id;

    private Long botId;
    private String contact;

    private String path;


    private int deilyTime;

}
