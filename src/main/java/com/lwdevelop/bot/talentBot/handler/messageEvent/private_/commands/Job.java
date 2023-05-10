package com.lwdevelop.bot.talentBot.handler.messageEvent.private_.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.lwdevelop.bot.talentBot.utils.Common;
import com.lwdevelop.bot.talentBot.utils.KeyboardButton;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class Job {

        @Autowired
        private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
                        .getBean(JobManagementServiceImpl.class);

        @Autowired
        private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
                        .getBean(SpringyBotServiceImpl.class);

        public void postRecruitment(Common common) {

                // send to channel
                SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();
                Iterator<RobotChannelManagement> iterator = springyBot.getRobotChannelManagement().iterator();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                springyBot.getJobUser().stream().filter(ju -> ju.getUserId().equals(userId))
                                .findFirst().ifPresent(j -> {
                                        j.getJobPosting().stream().filter(jp -> jp.getUserId().equals(userId))
                                                        .findFirst().ifPresent(
                                                                        jobPosting -> {
                                                                                while (iterator.hasNext()) {
                                                                                        sendTextWithJobPosting(
                                                                                                        jobPosting,
                                                                                                        common,
                                                                                                        iterator.next());
                                                                                }
                                                                        });
                                });
                ;

        }

        public void postAJobSearch(Common common) {
                SpringyBot springyBot = springyBotServiceImpl.findById(common.getSpringyBotId()).get();

                // send to channel
                Iterator<RobotChannelManagement> iterator = springyBot.getRobotChannelManagement()
                                .iterator();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                springyBot.getJobUser().stream().filter(ju -> ju.getUserId().equals(userId)).findFirst()
                                .ifPresent(j -> {
                                        j.getJobSeeker()
                                                        .stream()
                                                        .filter(
                                                                        jp -> jp.getUserId().equals(userId))
                                                        .findFirst()
                                                        .ifPresent(
                                                                        jobSeeker -> {
                                                                                while (iterator
                                                                                                .hasNext()) {
                                                                                        this.sendTextWithJobSeeker(
                                                                                                        jobSeeker,
                                                                                                        common,
                                                                                                        iterator
                                                                                                                        .next());
                                                                                }
                                                                        });
                                });
                ;
        }

        public void saveJobUser(Common common) {
                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                String firstname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getFirstName())
                                .orElse("");
                String username = Optional.ofNullable(common.getUpdate().getMessage().getChat().getUserName())
                                .orElse("");
                String lastname = Optional.ofNullable(common.getUpdate().getMessage().getChat().getLastName())
                                .orElse("");

                SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();

                springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                                .ifPresentOrElse(ju -> {
                                        ju.setFirstname(firstname);
                                        ju.setLastname(lastname);
                                        ju.setUsername(username);
                                }, () -> {
                                        JobUser jobUser = new JobUser();
                                        jobUser.setUserId(userId);
                                        jobUser.setFirstname(firstname);
                                        jobUser.setLastname(lastname);
                                        jobUser.setUsername(username);
                                        springyBot.getJobUser().add(jobUser);
                                });
                springyBotServiceImpl.save(springyBot);

        }

        public void setResponse_jobPosting_management(Common common) {

                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

                SendMessage response = new SendMessage();
                response.setChatId(userId);
                response.setText("提醒：用户只能发布一条讯息,无填写的栏位则跳过,招聘和求职信息管理帮助其他操作");
                common.sendResponseAsync(response);

                JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,
                                String.valueOf(id));

                SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
                JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                                .orElseGet(() -> {
                                        JobUser ju = new JobUser();
                                        ju.setUserId(userId);
                                        springyBot.getJobUser().add(ju);
                                        return ju;
                                });

                JobPostingDTO jobPostingDTO = new JobPostingDTO(userId, String.valueOf(id));
                String company = "", position = "", baseSalary = "", commission = "", workTime = "", requirements = "",
                                location = "", flightNumber = "";

                if (jobPosting != null) {
                        company = Optional.ofNullable(jobPosting.getCompany()).orElse("");
                        position = Optional.ofNullable(jobPosting.getPosition()).orElse("");
                        baseSalary = Optional.ofNullable(jobPosting.getBaseSalary()).orElse("");
                        commission = Optional.ofNullable(jobPosting.getCommission()).orElse("");
                        workTime = Optional.ofNullable(jobPosting.getWorkTime()).orElse("");
                        requirements = Optional.ofNullable(jobPosting.getRequirements()).orElse("");
                        location = Optional.ofNullable(jobPosting.getLocation()).orElse("");
                        flightNumber = Optional.ofNullable(jobPosting.getFlightNumber()).orElse("");

//                        jobPostingDTO = new JobPostingDTO(userId, String.valueOf(id), company, position, baseSalary,
//                                        commission, workTime, requirements, location, flightNumber);

                        // public JobPostingDTO(String userId, String botId, String company, String
                        // position, String baseSalary,
                        // String commission, String workTime, String requirements, String location,
                        // String flightNumber) {

                        response.setText(
                                        "招聘人才\n\n" + "公司：" + company + "\n" + "职位：" + position + "\n" + "底薪："
                                                        + baseSalary + "\n" + "提成：" + commission + "\n" + "上班时间："
                                                        + workTime + "\n" + "要求内容：" + requirements + "\n"
                                                        + "🐌地址：" + location + "\n" + "✈️咨询飞机号：" + flightNumber);
                        response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO,false));
                        // response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO));
                        Integer messageId = common.sendResponseAsync(response);
                        jobPosting.setLastMessageId(messageId);
                        jobUser.getJobPosting().add(jobPosting);
                        jobManagementServiceImpl.saveJobPosting(jobPosting);
                } else {
                        response.setText("招聘人才\n\n" + "公司：\n" + "职位：\n" + "底薪：\n" + "提成：\n" + "上班时间：\n" + "要求内容：\n"
                                        + "🐌地址：\n" + "✈️咨询飞机号：");
                        response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO,false));
                        // response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO));

                        JobPosting jp = new JobPosting(userId, String.valueOf(id),
                                        common.sendResponseAsync(response));
                        jobUser.getJobPosting().add(jp);
                        jobManagementServiceImpl.saveJobPosting(jp);
                        springyBotServiceImpl.save(springyBot);
                }
        }

        public void setResponse_jobSeeker_management(Common common) {

                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

                SendMessage response = new SendMessage();
                response.setChatId(String.valueOf(common.getUpdate().getMessage().getChatId()));
                response.setText("提醒：用户只能发布一条讯息,无填写的栏位则跳过,招聘和求职信息管理帮助其他操作");
                response.setDisableNotification(false);
                response.setDisableWebPagePreview(false);
                common.sendResponseAsync(response);

                JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId,
                                String.valueOf(id));

                SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();

                JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                                .orElseGet(() -> {
                                        JobUser ju = new JobUser();
                                        ju.setUserId(userId);
                                        springyBot.getJobUser().add(ju);
                                        return ju;
                                });

                JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(userId, String.valueOf(id));
                String name = "", gender = "", dateOfBirth = "", age = "", nationality = "", education = "",
                                skills = "", targetPosition = "", resources = "", expectedSalary = "",
                                workExperience = "", selfIntroduction = "", flightNumber = "";

                if (jobSeeker != null) {
                        name = Optional.ofNullable(jobSeeker.getName()).orElse("");
                        gender = Optional.ofNullable(jobSeeker.getGender()).orElse("");
                        dateOfBirth = Optional.ofNullable(jobSeeker.getDateOfBirth()).orElse("");
                        age = Optional.ofNullable(jobSeeker.getAge()).orElse("");
                        nationality = Optional.ofNullable(jobSeeker.getNationality()).orElse("");
                        education = Optional.ofNullable(jobSeeker.getEducation()).orElse("");
                        skills = Optional.ofNullable(jobSeeker.getSkills()).orElse("");
                        targetPosition = Optional.ofNullable(jobSeeker.getTargetPosition()).orElse("");
                        resources = Optional.ofNullable(jobSeeker.getResources()).orElse("");
                        expectedSalary = Optional.ofNullable(jobSeeker.getExpectedSalary()).orElse("");
                        workExperience = Optional.ofNullable(jobSeeker.getWorkExperience()).orElse("");
                        selfIntroduction = Optional.ofNullable(jobSeeker.getSelfIntroduction()).orElse("");
                        flightNumber = Optional.ofNullable(jobSeeker.getFlightNumber()).orElse("");
//
//                        jobSeekerDTO = new JobSeekerDTO(userId, String.valueOf(id), name, gender, dateOfBirth, age,
//                                        nationality, education, skills, targetPosition, resources, expectedSalary,
//                                        workExperience, selfIntroduction, flightNumber);

                        response.setText("求职人员\n\n姓名：" + name + "\n男女：" + gender + "\n出生_年_月_日："
                                        + dateOfBirth
                                        + "\n年龄：" + age + "\n国籍：" + nationality + "\n学历：" + education
                                        + "\n技能：" + skills + "\n目标职位：" + targetPosition + "\n手上有什么资源："
                                        + resources + "\n期望薪资：" + expectedSalary + "\n工作经历："
                                        + workExperience + "\n自我介绍：" + selfIntroduction + "\n✈️咨询飞机号：" + flightNumber);
                        response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO,false));
                        // response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO));
                        Integer messageId = common.sendResponseAsync(response);
                        jobSeeker.setLastMessageId(messageId);
                        jobUser.getJobSeeker().add(jobSeeker);
                        jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                } else {
                        response.setText(
                                        "求职人员\n\n姓名：\n男女：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作经历：\n自我介绍：\n✈️咨询飞机号：");
                        response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO,false));
                        // response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO));
                        JobSeeker js = new JobSeeker(userId, String.valueOf(id),
                                        common.sendResponseAsync(response));
                        jobUser.getJobSeeker().add(js);
                        jobManagementServiceImpl.saveJobSeeker(js);
                        springyBotServiceImpl.save(springyBot);
                }
        }

        public void setResponse_edit_jobPosting_management(Common common) {

                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

                SendMessage response = new SendMessage();
                response.setChatId(userId);

                List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String.valueOf(id),
                                                userId, "jobPosting");
                Map<String, Integer> channelInfo = channelMessageIdPostCounts.stream()
                                .collect(Collectors.toMap(
                                                ChannelMessageIdPostCounts::getChannelTitle,
                                                ChannelMessageIdPostCounts::getPostCount));

                String alert = channelInfo.entrySet().stream()
                                .map(entry -> entry.getValue() != 0
                                                ? entry.getKey() + " 发布了" + entry.getValue() + "则 [招聘人才] 信息\n"
                                                : "")
                                .collect(Collectors.joining());
                if (!alert.isEmpty()) {
                        response.setText("提醒：您已经在:\n" + alert);
                        response.setDisableNotification(true);
                        response.setDisableWebPagePreview(false);
                        common.sendResponseAsync(response);
                }
                JobPosting jobPosting = jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,
                                String.valueOf(id));

                SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();
                JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                                .orElseGet(() -> {
                                        JobUser ju = new JobUser();
                                        ju.setUserId(userId);
                                        springyBot.getJobUser().add(ju);
                                        return ju;
                                });

                JobPostingDTO jobPostingDTO = new JobPostingDTO(userId, String.valueOf(id));
                String company = "", position = "", baseSalary = "", commission = "", workTime = "", requirements = "",
                                location = "", flightNumber = "";

                if (jobPosting != null) {
                        company = Optional.ofNullable(jobPosting.getCompany()).orElse("");
                        position = Optional.ofNullable(jobPosting.getPosition()).orElse("");
                        baseSalary = Optional.ofNullable(jobPosting.getBaseSalary()).orElse("");
                        commission = Optional.ofNullable(jobPosting.getCommission()).orElse("");
                        workTime = Optional.ofNullable(jobPosting.getWorkTime()).orElse("");
                        requirements = Optional.ofNullable(jobPosting.getRequirements()).orElse("");
                        location = Optional.ofNullable(jobPosting.getLocation()).orElse("");
                        flightNumber = Optional.ofNullable(jobPosting.getFlightNumber()).orElse("");
                        response.setText(
                                        "招聘人才\n\n" + "公司：" + company + "\n" + "职位：" + position + "\n" + "底薪："
                                                        + baseSalary + "\n" + "提成：" + commission + "\n" + "上班时间："
                                                        + workTime + "\n" + "要求内容：" + requirements + "\n"
                                                        + "🐌地址：" + location + "\n" + "✈️咨询飞机号：" + flightNumber);
//                        jobPostingDTO = new JobPostingDTO(userId, String.valueOf(id), company, position, baseSalary,
//                                        commission, workTime, requirements, location, flightNumber);
                        response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO,true));
                        // response.setReplyMarkup(new KeyboardButton().keyboard_editJobPosting(jobPostingDTO));
                        Integer messageId = common.sendResponseAsync(response);
                        jobPosting.setLastMessageId(messageId);
                        jobUser.getJobPosting().add(jobPosting);
                        jobManagementServiceImpl.saveJobPosting(jobPosting);
                } else {
                        response.setText("招聘人才\n\n" + "公司：\n" + "职位：\n" + "底薪：\n" + "提成：\n" + "上班时间：\n" + "要求内容：\n"
                                        + "🐌地址：\n" + "✈️咨询飞机号：");
                        response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO,false));
                        // response.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO));

                        JobPosting jp = new JobPosting(userId, String.valueOf(id),
                                        common.sendResponseAsync(response));
                        jobUser.getJobPosting().add(jp);
                        jobManagementServiceImpl.saveJobPosting(jp);
                        springyBotServiceImpl.save(springyBot);
                }
        }

        public void setResponse_edit_jobSeeker_management(Common common) {

                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());

                SendMessage response = new SendMessage();
                response.setChatId(String.valueOf(common.getUpdate().getMessage().getChatId()));

                List<ChannelMessageIdPostCounts> channelMessageIdPostCounts = jobManagementServiceImpl
                                .findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String.valueOf(id),
                                                userId, "jobSeeker");
                Map<String, Integer> channelInfo = channelMessageIdPostCounts.stream()
                                .collect(Collectors.toMap(
                                                ChannelMessageIdPostCounts::getChannelTitle,
                                                ChannelMessageIdPostCounts::getPostCount));

                // String alert = channelInfo.entrySet().stream()
                // .map(entry -> entry.getKey() + " 发布了" + entry.getValue() + "则 [求职人员] 信息\n")
                // .collect(Collectors.joining());

                String alert = channelInfo.entrySet().stream()
                                .map(entry -> entry.getValue() != 0
                                                ? entry.getKey() + " 发布了" + entry.getValue() + "则 [求职人员] 信息\n"
                                                : "")
                                .collect(Collectors.joining());
                if (!alert.isEmpty()) {
                        response.setText("提醒：您已经在:\n" + alert);
                        response.setDisableNotification(false);
                        response.setDisableWebPagePreview(false);
                        common.sendResponseAsync(response);
                }
                JobSeeker jobSeeker = jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId,
                                String.valueOf(id));

                SpringyBot springyBot = springyBotServiceImpl.findById(id).orElseThrow();

                JobUser jobUser = springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                                .orElseGet(() -> {
                                        JobUser ju = new JobUser();
                                        ju.setUserId(userId);
                                        springyBot.getJobUser().add(ju);
                                        return ju;
                                });

                JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(userId, String.valueOf(id));
                String name = "", gender = "", dateOfBirth = "", age = "", nationality = "", education = "",
                                skills = "", targetPosition = "", resources = "", expectedSalary = "",
                                workExperience = "", selfIntroduction = "", flightNumber = "";

                if (jobSeeker != null) {
                        name = Optional.ofNullable(jobSeeker.getName()).orElse("");
                        gender = Optional.ofNullable(jobSeeker.getGender()).orElse("");
                        dateOfBirth = Optional.ofNullable(jobSeeker.getDateOfBirth()).orElse("");
                        age = Optional.ofNullable(jobSeeker.getAge()).orElse("");
                        nationality = Optional.ofNullable(jobSeeker.getNationality()).orElse("");
                        education = Optional.ofNullable(jobSeeker.getEducation()).orElse("");
                        skills = Optional.ofNullable(jobSeeker.getSkills()).orElse("");
                        targetPosition = Optional.ofNullable(jobSeeker.getTargetPosition()).orElse("");
                        resources = Optional.ofNullable(jobSeeker.getResources()).orElse("");
                        expectedSalary = Optional.ofNullable(jobSeeker.getExpectedSalary()).orElse("");
                        workExperience = Optional.ofNullable(jobSeeker.getWorkExperience()).orElse("");
                        selfIntroduction = Optional.ofNullable(jobSeeker.getSelfIntroduction()).orElse("");
                        flightNumber = Optional.ofNullable(jobSeeker.getFlightNumber()).orElse("");

//                        jobSeekerDTO = new JobSeekerDTO(userId, String.valueOf(id), name, gender, dateOfBirth, age,
//                                        nationality, education, skills, targetPosition, resources, expectedSalary,
//                                        workExperience, selfIntroduction, flightNumber);

                        response.setText("求职人员\n\n姓名：" + name + "\n男女：" + gender + "\n出生_年_月_日："
                                        + dateOfBirth
                                        + "\n年龄：" + age + "\n国籍：" + nationality + "\n学历：" + education
                                        + "\n技能：" + skills + "\n目标职位：" + targetPosition + "\n手上有什么资源："
                                        + resources + "\n期望薪资：" + expectedSalary + "\n工作经历："
                                        + workExperience + "\n自我介绍：" + selfIntroduction + "\n✈️咨询飞机号：" + flightNumber);
                        response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO,true));
                        // response.setReplyMarkup(new KeyboardButton().keyboard_editJobSeeker(jobSeekerDTO));
                        Integer messageId = common.sendResponseAsync(response);
                        jobSeeker.setLastMessageId(messageId);
                        jobUser.getJobSeeker().add(jobSeeker);
                        jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                } else {
                        response.setText(
                                        "求职人员\n\n姓名：\n男女：\n出生_年_月_日：\n年龄：\n国籍：\n学历：\n技能：\n目标职位：\n手上有什么资源：\n期望薪资：\n工作经历：\n自我介绍：\n✈️咨询飞机号：");
                        response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO,true));
                        // response.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO));
                        JobSeeker js = new JobSeeker(userId, String.valueOf(id),
                                        common.sendResponseAsync(response));
                        jobUser.getJobSeeker().add(js);
                        jobManagementServiceImpl.saveJobSeeker(js);
                        springyBotServiceImpl.save(springyBot);
                }
        }

        private void sendTextWithJobSeeker(JobSeeker jobSeeker, Common common,
                        RobotChannelManagement robotChannelManagement) {

                StringBuilder sb = new StringBuilder();
                appendIfNotEmpty(sb, "姓名：", jobSeeker.getName());
                appendIfNotEmpty(sb, "男女：", jobSeeker.getGender());
                appendIfNotEmpty(sb, "出生_年_月_日：", jobSeeker.getDateOfBirth());
                appendIfNotEmpty(sb, "年龄：", jobSeeker.getAge());
                appendIfNotEmpty(sb, "国籍：", jobSeeker.getNationality());
                appendIfNotEmpty(sb, "学历：", jobSeeker.getEducation());
                appendIfNotEmpty(sb, "技能：", jobSeeker.getSkills());
                appendIfNotEmpty(sb, "目标职位：", jobSeeker.getTargetPosition());
                appendIfNotEmpty(sb, "手上有什么资源：", jobSeeker.getResources());
                appendIfNotEmpty(sb, "期望薪资：", jobSeeker.getExpectedSalary());
                appendIfNotEmpty(sb, "工作经历：", jobSeeker.getWorkExperience());
                appendIfNotEmpty(sb, "自我介绍：", jobSeeker.getSelfIntroduction());
                appendIfNotEmpty(sb, "✈️咨询飞机号：", jobSeeker.getFlightNumber());
                String result = sb.toString().trim(); // 去掉前后空格

                SendMessage response = new SendMessage();
                if (!result.isEmpty()) {
                        String username = common.getUpdate().getMessage().getChat().getUserName();
                        response.setChatId(String.valueOf(robotChannelManagement.getChannelId()));
                        response.setText("求职人员\n\n" + result);
                        response.setReplyMarkup(new KeyboardButton().keyboard_callme(username));
                        common.sendResponseAsync(response);
                } else {
                        Long chatId = common.getUpdate().getMessage().getChatId();
                        response.setChatId(String.valueOf(chatId));
                        response.setText("尚未编辑招聘表单");
                        common.sendResponseAsync(response);
                }

        }

        private void sendTextWithJobPosting(JobPosting jobPosting, Common common,
                        RobotChannelManagement robotChannelManagement) {
                StringBuilder sb = new StringBuilder();
                appendIfNotEmpty(sb, "公司：", jobPosting.getCompany());
                appendIfNotEmpty(sb, "职位：", jobPosting.getPosition());
                appendIfNotEmpty(sb, "底薪：", jobPosting.getBaseSalary());
                appendIfNotEmpty(sb, "提成：", jobPosting.getCommission());
                appendIfNotEmpty(sb, "上班时间：", jobPosting.getWorkTime());
                appendIfNotEmpty(sb, "要求内容：", jobPosting.getRequirements());
                appendIfNotEmpty(sb, "🐌地址：", jobPosting.getLocation());
                appendIfNotEmpty(sb, "✈️咨询飞机号：", jobPosting.getFlightNumber());
                String result = sb.toString().trim(); // 去掉前后空格

                SendMessage response = new SendMessage();
                if (!result.isEmpty()) {
                        String username = common.getUpdate().getMessage().getChat().getUserName();
                        response.setChatId(String.valueOf(robotChannelManagement.getChannelId()));
                        response.setText("招聘人才\n\n" + result);
                        response.setReplyMarkup(new KeyboardButton().keyboard_callme(username));
                        common.sendResponseAsync(response);
                } else {
                        Long chatId = common.getUpdate().getMessage().getChatId();
                        response.setChatId(String.valueOf(chatId));
                        response.setText("尚未编辑招聘表单");
                        common.sendResponseAsync(response);
                }
        }

        private void appendIfNotEmpty(StringBuilder sb, String label, String value) {
                if (value != null && !value.isEmpty()) {
                        sb.append(label).append(value).append("\n");
                }
        }

}
