package com.lwdevelop.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 招聘信息ID

    private String userId;

    private String company; // 公司名称

    private String position; // 职位名称

    private Integer baseSalary; // 底薪

    private Integer commission; // 提成

    private String workTime; // 上班时间

    private String requirements; // 要求内容

    private String location; // 地址
    
    private String flightNumber; // 咨询飞机号
}
