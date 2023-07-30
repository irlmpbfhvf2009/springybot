package org.springybot.dto;

import java.util.Optional;

import org.springybot.botModel.Common;
import org.springybot.botModel.enum_.TalentEnum;
import org.springybot.entity.JobPosting;

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

    private String gender; // 男女

    private String headCounts; // 人數

    private String languages; // 语言要求

    private String agency; // 是否中介

    private String workTime; // 上班时间

    private String requirements; // 要求内容

    private String location; // 地址

    private String flightNumber; // 咨询飞机号

    private String publisher; // 發布人

    private Common common;

    public JobPostingDTO(Common common) {
        this.common = common;
    }

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
        this.publisher = "";
    }

    public JobPostingDTO(String userId, String botId, String company, String position, String baseSalary,
            String commission, String nationality, String gender, String headCounts, String languages, String agency,
            String workTime, String requirements, String location, String flightNumber, String publisher) {
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
        this.publisher = publisher;
    }

    public JobPosting resetJobPostingFields(JobPosting jobPosting) {
        jobPosting.setBaseSalary("");
        jobPosting.setCommission("");
        jobPosting.setNationality("");
        jobPosting.setGender("");
        jobPosting.setHeadCounts("");
        jobPosting.setLanguages("");
        jobPosting.setAgency("");
        jobPosting.setCompany("");
        jobPosting.setFlightNumber("");
        jobPosting.setLocation("");
        jobPosting.setPosition("");
        jobPosting.setRequirements(TalentEnum.FIFTY_CHARACTERS_LIMIT.getText());
        jobPosting.setWorkTime("");
        jobPosting.setPublisher("");
        return jobPosting;
    }

    public JobPostingDTO createJobPostingDTO(String userId, String id) {
        return new JobPostingDTO(userId, id, this.company, this.position, this.baseSalary,
                this.commission, this.nationality, this.gender, this.headCounts, this.languages, agency, workTime,
                this.requirements,
                this.location, this.flightNumber, this.publisher);
    }

    public JobPostingDTO convertToJobPostingDTO(JobPosting jobPosting) {
        return new JobPostingDTO(jobPosting.getUserId(), jobPosting.getBotId(), jobPosting.getCompany(),
                jobPosting.getPosition(), jobPosting.getBaseSalary(),
                jobPosting.getCommission(), jobPosting.getNationality(), jobPosting.getGender(),
                jobPosting.getHeadCounts(), jobPosting.getLanguages(), jobPosting.getAgency(), jobPosting.getWorkTime(),
                jobPosting.getRequirements(),
                jobPosting.getLocation(), jobPosting.getFlightNumber(), jobPosting.getPublisher());
    }

    public String generateJobPostingResponse(JobPosting jobPosting, Boolean isEdit) {

        this.company = Optional.ofNullable(jobPosting.getCompany()).orElse("");
        this.position = Optional.ofNullable(jobPosting.getPosition()).orElse("");
        this.baseSalary = Optional.ofNullable(jobPosting.getBaseSalary()).orElse("");
        this.commission = Optional.ofNullable(jobPosting.getCommission()).orElse("");
        this.nationality = Optional.ofNullable(jobPosting.getNationality()).orElse("");
        this.gender = Optional.ofNullable(jobPosting.getGender()).orElse("");
        this.headCounts = Optional.ofNullable(jobPosting.getHeadCounts()).orElse("");
        this.languages = Optional.ofNullable(jobPosting.getLanguages()).orElse("");
        this.agency = Optional.ofNullable(jobPosting.getAgency()).orElse("");
        this.workTime = Optional.ofNullable(jobPosting.getWorkTime()).orElse("");
        this.requirements = Optional.ofNullable(jobPosting.getRequirements())
                .orElse(TalentEnum.FIFTY_CHARACTERS_LIMIT.getText());
        this.location = Optional.ofNullable(jobPosting.getLocation()).orElse("");
        this.flightNumber = Optional.ofNullable(jobPosting.getFlightNumber()).orElse("");
        this.publisher = Optional.ofNullable(jobPosting.getPublisher()).orElse("");

        String str = isEdit == true ? "编辑招聘" : "招聘人才";

        return str + "\n\n公司：" + company + "\n职位：" + position + "\n底薪："
                + baseSalary + "\n" + "提成：" + commission + "\n" + "国籍：" + nationality + "\n"
                + "男女：" + gender + "\n人数：" + headCounts + "\n语言要求：" + languages + "\n是否中介：" + agency + "\n上班时间："
                + workTime + "\n要求内容：" + requirements + "\n"
                + "🐌地址：" + location + "\n✈️咨询飞机号：" + flightNumber;
    }

    public String generateJobPostingDetails(JobPosting jobPosting) {
        StringBuilder sb = new StringBuilder();
        appendIfNotEmpty(sb, "公司：", jobPosting.getCompany());
        appendIfNotEmpty(sb, "职位：", jobPosting.getPosition());
        appendIfNotEmpty(sb, "底薪：", jobPosting.getBaseSalary());
        appendIfNotEmpty(sb, "提成：", jobPosting.getCommission());
        appendIfNotEmpty(sb, "国籍: ", jobPosting.getNationality());
        appendIfNotEmpty(sb, "男女：", jobPosting.getGender());
        appendIfNotEmpty(sb, "人数：", jobPosting.getHeadCounts());
        appendIfNotEmpty(sb, "语言要求：", jobPosting.getLanguages());
        appendIfNotEmpty(sb, "是否中介：", jobPosting.getAgency());
        appendIfNotEmpty(sb, "上班时间：", jobPosting.getWorkTime());
        appendIfNotEmpty(sb, "要求内容：", jobPosting.getRequirements());
        appendIfNotEmpty(sb, "🐌地址：", jobPosting.getLocation());
        appendIfNotEmpty(sb, "✈️咨询飞机号：", jobPosting.getFlightNumber());
        appendIfNotEmpty(sb, "发布人：", jobPosting.getPublisher());

        String result = sb.toString().trim();
        return result;
    }

    public String fillJobPostingInfo(JobPosting jobPosting, String[] lines) {
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
                    case "公司":
                        jobPosting.setCompany(value);
                        break;
                    case "职位":
                        jobPosting.setPosition(value);
                        break;
                    case "底薪":
                        jobPosting.setBaseSalary(value);
                        break;
                    case "提成":
                        jobPosting.setCommission(value);
                        break;
                    case "国籍":
                        jobPosting.setNationality(value);
                        break;
                    case "男女":
                        jobPosting.setGender(value);
                        break;
                    case "人数":
                        jobPosting.setHeadCounts(value);
                        break;
                    case "语言要求":
                        jobPosting.setLanguages(value);
                        break;
                    case "是否中介":
                        jobPosting.setAgency(value);
                        break;
                    case "上班时间":
                        jobPosting.setWorkTime(value);
                        break;
                    case "要求内容":
                        if (value.length() >= 50) {
                            returnStr = "发送失败,要求内容超过50字";
                        }
                        jobPosting.setRequirements(value);
                        break;
                    case "🐌地址":
                        jobPosting.setLocation(value);
                        break;
                    case "✈️咨询飞机号":
                        jobPosting.setFlightNumber(value);
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
