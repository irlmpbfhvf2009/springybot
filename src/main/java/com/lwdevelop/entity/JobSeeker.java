package com.lwdevelop.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 求职者ID

    private String userId;

    private String botId;

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
    
    private Integer lastMessageId;

    public JobSeeker(String userId, String botId, Integer lastMessageId) {
        this.userId = userId;
        this.botId = botId;
        this.lastMessageId = lastMessageId;
    }
}
