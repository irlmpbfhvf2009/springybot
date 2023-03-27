package com.lwdevelop.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class JobSeekerDTO {
    
    private Long id; // 求职者ID

    private String name; // 姓名

    private String gender; // 性别

    private LocalDate dateOfBirth; // 出生日期

    private Integer age; // 年龄

    private String nationality; // 国籍

    private String education; // 学历

    private String skills; // 技能

    private String targetPosition; // 目标职位

    private String resources; // 手上资源

    private Integer expectedSalary; // 期望薪资

    private String workExperience; // 工作经历

    private String selfIntroduction; // 自我介绍
}
