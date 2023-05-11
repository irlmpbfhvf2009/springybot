package com.lwdevelop.bot.talentBot.utils.jobLibrary;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.lwdevelop.bot.talentBot.utils.Common;
import com.lwdevelop.bot.talentBot.utils.SpringyBotEnum;
import com.lwdevelop.entity.JobPosting;

public class Posting {
    Common common;

    public Posting(Common common){
        this.common = common;
        
    }


    public String fillJobPostingInfo(JobPosting jobPosting, String[] lines) {
        String returnStr = "";
        for (String line : lines) {
            int colonIndex = line.indexOf("：");
            if (colonIndex >= 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();

                String filter = SpringyBotEnum.FIFTYCHARACTERSLIMIT.getText();
                value = value.replace(filter, "");

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
                            returnStr = SpringyBotEnum.REMINDREQUIREMENTSLIMIT.getText();
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
                        // 未知键值对，可以忽略或抛出异常
                        SendMessage response = new SendMessage(jobPosting.getUserId(), SpringyBotEnum.FILTERKEY.getText() + key);
                        this.common.sendResponseAsync(response);
                        break;
                }
            }
        }
        return returnStr;
    }
}
