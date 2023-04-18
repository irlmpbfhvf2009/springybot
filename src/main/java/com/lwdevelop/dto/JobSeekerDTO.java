package com.lwdevelop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerDTO {

    private Long id; // 求职者ID

    private String ub; //

    private String userId;

    private String botId;

    private String name; // 姓名

    private String gender; // 性别

    private String dateOfBirth; // 出生日期

    private String age; // 年龄

    private String nationality; // 国籍

    private String education; // 学历

    private String skills; // 技能

    private String targetPosition; // 目标职位

    private String resources; // 手上资源

    private String expectedSalary; // 期望薪资

    private String workExperience; // 工作经历

    private String selfIntroduction; // 自我介绍

    private String flightNumber; // 咨询飞机号

    public JobSeekerDTO(String userId, String botId) {
        this.userId = userId;
        this.botId = botId;
        this.id = null;
        this.ub = null;
        this.name = "";
        this.gender = "";
        this.dateOfBirth = "";
        this.age = "";
        this.nationality = "";
        this.education = "";
        this.skills = "";
        this.targetPosition = "";
        this.resources = "";
        this.expectedSalary = "";
        this.workExperience = "";
        this.selfIntroduction = "";
        this.flightNumber ="";
    }

    public JobSeekerDTO(String userId, String botId, String name, String gender, String dateOfBirth, String age,
            String nationality, String education, String skills, String targetPosition, String resources,
            String expectedSalary, String workExperience, String selfIntroduction,String flightNumber) {
        this.userId = userId;
        this.botId = botId;
        this.id = null;
        this.ub = null;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.nationality = nationality;
        this.education = education;
        this.skills = skills;
        this.targetPosition = targetPosition;
        this.resources = resources;
        this.expectedSalary = expectedSalary;
        this.workExperience = workExperience;
        this.selfIntroduction = selfIntroduction;
        this.flightNumber = flightNumber;
    }

}
