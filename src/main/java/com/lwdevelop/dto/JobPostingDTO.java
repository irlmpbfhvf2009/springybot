package com.lwdevelop.dto;

import lombok.Data;

@Data
public class JobPostingDTO {
    
    private Long id; // 招聘信息ID

    private String ub; // userId and botId

    private String userId;

    private String botId;

    private String company; // 公司名称

    private String position; // 职位名称

    private String baseSalary; // 底薪

    private String commission; // 提成

    private String workTime; // 上班时间

    private String requirements; // 要求内容

    private String location; // 地址

    private String flightNumber; // 咨询飞机号
}
