package org.springybot.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springybot.botModel.Common;
import org.springybot.botModel.enum_.TalentEnum;
import org.springybot.entity.JobSeeker;

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

    private String headCounts; // 人數

    private String dateOfBirth; // 出生日期

    private String age; // 年龄

    private String nationality; // 国籍

    private String education; // 学历

    private String skills; // 技能

    private String targetPosition; // 目标职位

    private String resources; // 手上资源

    private String expectedSalary; // 期望薪资

    private String workingAddress; // 工作地址

    private String language; // 精通语言

    private String workExperience; // 工作经历

    private String selfIntroduction; // 自我介绍

    private String flightNumber; // 咨询飞机号

    private String publisher;   //發布人

    private Common common;

    // 发布后 channelId [0]messageId 訊息ID [1]postCount 發送次數
    private HashMap<Integer, ArrayList<Integer>> channelMessageIdPostCount;

    public JobSeekerDTO(Common common) {
        this.common = common;
    }

    public JobSeekerDTO(String userId, String botId) {
        this.userId = userId;
        this.botId = botId;
        this.id = null;
        this.ub = null;
        this.name = "";
        this.gender = "";
        this.headCounts = "";
        this.dateOfBirth = "";
        this.age = "";
        this.nationality = "";
        this.education = "";
        this.skills = "";
        this.targetPosition = "";
        this.resources = "";
        this.expectedSalary = "";
        this.workingAddress = "";
        this.language = "";
        this.workExperience = "";
        this.selfIntroduction = "";
        this.flightNumber = "";
        this.publisher = "";
    }

    public JobSeekerDTO(String userId, String botId, String name, String gender, String headCounts, String dateOfBirth,
            String age,
            String nationality, String education, String skills, String targetPosition, String resources,
            String expectedSalary, String workingAddress, String language, String workExperience,
            String selfIntroduction, String flightNumber, String publisher) {
        this.userId = userId;
        this.botId = botId;
        this.id = null;
        this.ub = null;
        this.name = name;
        this.gender = gender;
        this.headCounts = headCounts;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.nationality = nationality;
        this.education = education;
        this.skills = skills;
        this.targetPosition = targetPosition;
        this.resources = resources;
        this.expectedSalary = expectedSalary;
        this.workingAddress = workingAddress;
        this.language = language;
        this.workExperience = workExperience;
        this.selfIntroduction = selfIntroduction;
        this.flightNumber = flightNumber;
        this.publisher = publisher;
    }

    public JobSeeker resetJobSeekerFields(JobSeeker jobSeeker) {
        jobSeeker.setName("");
        jobSeeker.setGender("");
        jobSeeker.setHeadCounts("");
        jobSeeker.setDateOfBirth("");
        jobSeeker.setAge("");
        jobSeeker.setNationality("");
        jobSeeker.setEducation("");
        jobSeeker.setSkills("");
        jobSeeker.setTargetPosition("");
        jobSeeker.setResources("");
        jobSeeker.setExpectedSalary("");
        jobSeeker.setWorkingAddress("");
        jobSeeker.setLanguage("");
        jobSeeker.setWorkExperience("（限50字以内）");
        jobSeeker.setSelfIntroduction("（限50字以内）");
        jobSeeker.setFlightNumber("");
        jobSeeker.setPublisher("");
        return jobSeeker;
    }

    public JobSeekerDTO createJobSeekerDTO(String userId, String id) {
        return new JobSeekerDTO(userId, id, this.name, this.gender, this.headCounts, this.dateOfBirth, this.age,
                this.nationality, this.education, this.skills, this.targetPosition, this.resources, this.expectedSalary,
                this.workingAddress,
                this.language,
                this.workExperience, this.selfIntroduction, this.flightNumber, this.publisher);
    }

    public JobSeekerDTO convertToJobSeekerDTO(JobSeeker jobSeeker){
        return new JobSeekerDTO(jobSeeker.getUserId(), jobSeeker.getBotId(), jobSeeker.getName(),
        jobSeeker.getGender(), jobSeeker.getHeadCounts(), jobSeeker.getDateOfBirth(), jobSeeker.getAge(),
        jobSeeker.getNationality(),
        jobSeeker.getEducation(), jobSeeker.getSkills(), jobSeeker.getTargetPosition(),
        jobSeeker.getResources(), jobSeeker.getExpectedSalary(),
        jobSeeker.getWorkingAddress(), jobSeeker.getLanguage(), jobSeeker.getWorkExperience(),
        jobSeeker.getSelfIntroduction(), jobSeeker.getFlightNumber(), jobSeeker.getPublisher());
    }

    public String generateJobSeekerResponse(JobSeeker jobSeeker, Boolean isEdit) {
        this.name = Optional.ofNullable(jobSeeker.getName()).orElse("");
        this.gender = Optional.ofNullable(jobSeeker.getGender()).orElse("");
        this.headCounts = Optional.ofNullable(jobSeeker.getHeadCounts()).orElse("");
        this.dateOfBirth = Optional.ofNullable(jobSeeker.getDateOfBirth()).orElse("");
        this.age = Optional.ofNullable(jobSeeker.getAge()).orElse("");
        this.nationality = Optional.ofNullable(jobSeeker.getNationality()).orElse("");
        this.education = Optional.ofNullable(jobSeeker.getEducation()).orElse("");
        this.skills = Optional.ofNullable(jobSeeker.getSkills()).orElse("");
        this.targetPosition = Optional.ofNullable(jobSeeker.getTargetPosition()).orElse("");
        this.resources = Optional.ofNullable(jobSeeker.getResources()).orElse("");
        this.expectedSalary = Optional.ofNullable(jobSeeker.getExpectedSalary()).orElse("");
        this.workingAddress = Optional.ofNullable(jobSeeker.getWorkingAddress()).orElse("");
        this.language = Optional.ofNullable(jobSeeker.getLanguage()).orElse("");
        this.workExperience = Optional.ofNullable(jobSeeker.getWorkExperience())
                .orElse(TalentEnum.FIFTY_CHARACTERS_LIMIT.getText());
        this.selfIntroduction = Optional.ofNullable(jobSeeker.getSelfIntroduction())
                .orElse(TalentEnum.FIFTY_CHARACTERS_LIMIT.getText());
        this.flightNumber = Optional.ofNullable(jobSeeker.getFlightNumber()).orElse("");
        this.publisher = Optional.ofNullable(jobSeeker.getPublisher()).orElse("");

        String str = isEdit == true ? "编辑求职" : "求职人员";

        return str + "\n\n姓名：" + name + "\n男女：" + gender
                + "\n人数：" + headCounts
                + "\n出生_年_月_日：" + dateOfBirth
                + "\n年龄：" + age + "\n国籍：" + nationality + "\n学历：" + education
                + "\n技能：" + skills + "\n目标职位：" + targetPosition + "\n手上有什么资源："
                + resources + "\n期望薪资：" + expectedSalary
                + "\n工作地址：" + workingAddress + "\n精通语言：" + language
                + "\n工作经历："
                + workExperience + "\n自我介绍：" + selfIntroduction + "\n✈️咨询飞机号：" + flightNumber;
    }

    public String generateJobSeekerDetails(JobSeeker jobSeeker) {
        StringBuilder sb = new StringBuilder();
        appendIfNotEmpty(sb, "姓名：", jobSeeker.getName());
        appendIfNotEmpty(sb, "男女：", jobSeeker.getGender());
        appendIfNotEmpty(sb, "人数：", jobSeeker.getHeadCounts());
        appendIfNotEmpty(sb, "出生_年_月_日：", jobSeeker.getDateOfBirth());
        appendIfNotEmpty(sb, "年龄：", jobSeeker.getAge());
        appendIfNotEmpty(sb, "国籍：", jobSeeker.getNationality());
        appendIfNotEmpty(sb, "学历：", jobSeeker.getEducation());
        appendIfNotEmpty(sb, "技能：", jobSeeker.getSkills());
        appendIfNotEmpty(sb, "目标职位：", jobSeeker.getTargetPosition());
        appendIfNotEmpty(sb, "手上有什么资源：", jobSeeker.getResources());
        appendIfNotEmpty(sb, "期望薪资：", jobSeeker.getExpectedSalary());
        appendIfNotEmpty(sb, "工作地址：", jobSeeker.getWorkingAddress());
        appendIfNotEmpty(sb, "精通语言：", jobSeeker.getLanguage());
        appendIfNotEmpty(sb, "工作经历：", jobSeeker.getWorkExperience());
        appendIfNotEmpty(sb, "自我介绍：", jobSeeker.getSelfIntroduction());
        appendIfNotEmpty(sb, "✈️咨询飞机号：", jobSeeker.getFlightNumber());
        appendIfNotEmpty(sb, "发布人：", jobSeeker.getPublisher());

        String result = sb.toString().trim();
        return result;
    }

    public String fillJobSeekerInfo(JobSeeker jobSeeker, String[] lines) {
        String returnStr = "";
        for (String line : lines) {
            int colonIndex = line.indexOf("：");
            if (colonIndex >= 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();

                String filter = TalentEnum.FIFTY_CHARACTERS_LIMIT.getText();
                value = value.replace(filter, "");
                value = value.substring(0, Math.min(255, value.length()));

                switch (key) {
                    case "姓名":
                        jobSeeker.setName(value);
                        break;
                    case "男女":
                        jobSeeker.setGender(value);
                        break;
                    case "人数":
                        jobSeeker.setHeadCounts(value);
                        break;
                    case "出生_年_月_日":
                        jobSeeker.setDateOfBirth(value);
                        break;
                    case "年龄":
                        jobSeeker.setAge(value);
                        break;
                    case "国籍":
                        jobSeeker.setNationality(value);
                        break;
                    case "学历":
                        jobSeeker.setEducation(value);
                        break;
                    case "技能":
                        jobSeeker.setSkills(value);
                        break;
                    case "目标职位":
                        jobSeeker.setTargetPosition(value);
                        break;
                    case "手上有什么资源":
                        jobSeeker.setResources(value);
                        break;
                    case "期望薪资":
                        jobSeeker.setExpectedSalary(value);
                        break;
                    case "工作地址":
                        jobSeeker.setWorkingAddress(value);
                        break;
                    case "精通语言":
                        jobSeeker.setLanguage(value);
                        break;
                    case "工作经历":
                        if (value.length() >= 50) {
                            returnStr = "发送失败,工作经历超过50字";
                        }
                        jobSeeker.setWorkExperience(value);
                        break;
                    case "自我介绍":
                        if (value.length() >= 50) {
                            returnStr = "发送失败,自我介绍超过50字";
                        }
                        jobSeeker.setSelfIntroduction(value);
                        break;
                    case "✈️咨询飞机号":
                        jobSeeker.setFlightNumber(value);
                        break;
                    default:
                        break;
                }
            }
        }
        return returnStr;
    }

    private void appendIfNotEmpty(StringBuilder sb, String label, String value) {
        if (value != null && !value.isEmpty()) {
            sb.append(label).append(value).append("\n");
        }
    }
}
