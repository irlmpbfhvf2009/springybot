package com.lwdevelop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingDTO {

    private Long id; // 招聘信息ID

    private String ub;

    private String userId;

    private String botId;

    private String company; // 公司名称

    private String position; // 职位名称

    private String baseSalary; // 底薪

    private String commission; // 提成

    private String nationality; // 国籍

    private String gender;  // 男女

    private String headCounts;   //  人數

    private String languages;   // 语言要求

    private String agency;  //是否中介

    private String workTime; // 上班时间

    private String requirements; // 要求内容

    private String location; // 地址

    private String flightNumber; // 咨询飞机号

    public JobPostingDTO(String userId, String botId) {
        this.userId = userId;
        this.botId = botId;
        this.id = null;
        this.ub = null;
        this.company = "";
        this.position = "";
        this.baseSalary = "";
        this.commission = "";
        this.nationality = "";
        this.gender = "";
        this.headCounts = "";
        this.languages = "";
        this.agency = "";
        this.workTime = "";
        this.requirements = "";
        this.location = "";
        this.flightNumber = "";
    }

    public JobPostingDTO(String userId, String botId, String company, String position, String baseSalary,
            String commission, String nationality, String gender, String headCounts, String languages, String agency,
                         String workTime, String requirements, String location, String flightNumber) {
        this.userId = userId;
        this.botId = botId;
        this.id = null;
        this.ub = null;
        this.company = company;
        this.position = position;
        this.baseSalary = baseSalary;
        this.commission = commission;
        this.nationality = nationality;
        this.gender = gender;
        this.headCounts = headCounts;
        this.languages = languages;
        this.agency = agency;
        this.workTime = workTime;
        this.requirements = requirements;
        this.location = location;
        this.flightNumber = flightNumber;
    }
}
