package com.lwdevelop.bot.handler.messageEvent.private_.commands;

import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.lwdevelop.bot.utils.Common;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.service.impl.JobManagementServiceImpl;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.SpringUtils;

public class Job {
        SendMessage response;

        @Autowired
        private JobManagementServiceImpl jobManagementServiceImpl = SpringUtils.getApplicationContext()
                        .getBean(JobManagementServiceImpl.class);

        @Autowired
        private SpringyBotServiceImpl springyBotServiceImpl = SpringUtils.getApplicationContext()
                        .getBean(SpringyBotServiceImpl.class);

        private void jobMessageSetting(Message message) {
                this.response = new SendMessage();
                this.response.setChatId(String.valueOf(message.getChatId()));
                this.response.setDisableNotification(false);
                this.response.setDisableWebPagePreview(false);
        }

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
                                                                                                        iterator.next()
                                                                                                        );
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

        public void setResponse_jobSeeker_management(Common common) {
                this.jobMessageSetting(common.getUpdate().getMessage());
                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                String firstname = common.getUpdate().getMessage().getChat().getFirstName();
                String username = common.getUpdate().getMessage().getChat().getUserName();
                String lastname = common.getUpdate().getMessage().getChat().getLastName();

                if (firstname == null) {
                        firstname = "";
                }
                if (username == null) {
                        username = "";
                }
                if (lastname == null) {
                        lastname = "";
                }

                SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
                JobSeeker jobSeeker = this.jobManagementServiceImpl.findByUserIdAndBotIdWithJobSeeker(userId,
                                String.valueOf(common.getSpringyBotId()));
                JobUser jobUser = new JobUser();
                jobUser.setFirstname(firstname);
                jobUser.setLastname(lastname);
                jobUser.setUserId(userId);
                jobUser.setUsername(username);
                springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId)).findFirst()
                                .ifPresentOrElse(j -> {
                                        j.getJobSeeker().stream().filter(ju -> ju.getUserId().equals(userId))
                                                        .findFirst().ifPresentOrElse(js -> {
                                                                String name = js.getName() == null ? "" : js.getName();
                                                                String gender = js.getGender() == null ? ""
                                                                                : js.getGender();
                                                                String dateOfBirth = js.getDateOfBirth() == null ? ""
                                                                                : js.getDateOfBirth();
                                                                String age = js.getAge() == null ? "" : js.getAge();
                                                                String nationality = js.getNationality() == null ? ""
                                                                                : js.getNationality();
                                                                String education = js.getEducation() == null ? ""
                                                                                : js.getEducation();
                                                                String skills = js.getSkills() == null ? ""
                                                                                : js.getSkills();
                                                                String targetPosition = js.getTargetPosition() == null
                                                                                ? ""
                                                                                : js.getTargetPosition();
                                                                String resources = js.getResources() == null ? ""
                                                                                : js.getResources();
                                                                String expectedSalary = js.getExpectedSalary() == null
                                                                                ? ""
                                                                                : js.getExpectedSalary();
                                                                String workExperience = js.getWorkExperience() == null
                                                                                ? ""
                                                                                : js.getWorkExperience();
                                                                String selfIntroduction = js
                                                                                .getSelfIntroduction() == null ? ""
                                                                                                : js.getSelfIntroduction();

                                                                this.response.setText("求职人员\n" +
                                                                                "姓名：" + name + "\n" +
                                                                                "男女：" + gender + "\n" +
                                                                                "出生_年_月_日：" + dateOfBirth + "\n"
                                                                                +
                                                                                "年龄：" + age + "\n" +
                                                                                "国籍：" + nationality + "\n" +
                                                                                "学历：" + education + "\n" +
                                                                                "技能：" + skills + "\n" +
                                                                                "目标职位： " + targetPosition + "\n"
                                                                                +
                                                                                "手上有什么资源：" + resources + "\n"
                                                                                +
                                                                                "期望薪资：" + expectedSalary + "\n"
                                                                                +
                                                                                "工作经历：" + workExperience + "\n"
                                                                                +
                                                                                "自我介绍：" + selfIntroduction);

                                                                JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(userId,
                                                                                String.valueOf(id),
                                                                                name, gender, dateOfBirth, age,
                                                                                nationality, education, skills,
                                                                                targetPosition,
                                                                                resources,
                                                                                expectedSalary,
                                                                                workExperience,
                                                                                selfIntroduction);
                                                                this.response.setReplyMarkup(
                                                                                new KeyboardButton().keyboard_jobSeeker(
                                                                                                jobSeekerDTO));
                                                                Integer lastMessageId = common
                                                                                .sendResponseAsync(this.response);
                                                                jobSeeker.setLastMessageId(lastMessageId);
                                                                this.jobManagementServiceImpl.saveJobSeeker(jobSeeker);
                                                        }, () -> {
                                                                this.response.setText("求职人员\n" +
                                                                                "姓名：\n" +
                                                                                "男女：\n" +
                                                                                "出生_年_月_日\n" +
                                                                                "年龄：\n" +
                                                                                "国籍：\n" +
                                                                                "学历：\n" +
                                                                                "技能：\n" +
                                                                                "目标职位：\n" +
                                                                                "手上有什么资源：\n" +
                                                                                "期望薪资：\n" +
                                                                                "工作经历：\n" +
                                                                                "自我介绍：");
                                                                JobSeekerDTO jobSeekerDTO = new JobSeekerDTO(userId,
                                                                                String.valueOf(common
                                                                                                .getSpringyBotId()));
                                                                this.response.setReplyMarkup(
                                                                                new KeyboardButton().keyboard_jobSeeker(
                                                                                                jobSeekerDTO));
                                                                Integer lastMessageId = common
                                                                                .sendResponseAsync(this.response);
                                                                JobSeeker jobSeeker_ = new JobSeeker(userId,
                                                                                String.valueOf(common
                                                                                                .getSpringyBotId()),
                                                                                lastMessageId);
                                                                springyBot.getJobUser().stream().filter(
                                                                                ju -> ju.getUserId().equals(userId))
                                                                                .findFirst()
                                                                                .ifPresent(ju -> ju.getJobSeeker()
                                                                                                .add(jobSeeker_));
                                                                springyBotServiceImpl.save(springyBot);
                                                        });
                                }, () -> {
                                        springyBot.getJobUser().add(jobUser);
                                        springyBotServiceImpl.save(springyBot);
                                });

        }

        public void setResponse_jobPosting_management(Common common) {
                this.jobMessageSetting(common.getUpdate().getMessage());
                Long id = common.getSpringyBotId();
                String userId = String.valueOf(common.getUpdate().getMessage().getChatId());
                String firstname = common.getUpdate().getMessage().getChat().getFirstName();
                String username = common.getUpdate().getMessage().getChat().getUserName();
                String lastname = common.getUpdate().getMessage().getChat().getLastName();

                firstname = firstname == null ? "" : firstname;
                username = username == null ? "" : username;
                lastname = lastname == null ? "" : lastname;

                SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
                JobPosting jobPosting = this.jobManagementServiceImpl.findByUserIdAndBotIdWithJobPosting(userId,
                                String.valueOf(common.getSpringyBotId()));
                JobUser jobUser = new JobUser();
                jobUser.setFirstname(firstname);
                jobUser.setLastname(lastname);
                jobUser.setUserId(userId);
                jobUser.setUsername(username);
                springyBot.getJobUser().stream().filter(j -> j.getUserId().equals(userId))
                                .findFirst().ifPresentOrElse(j -> {
                                        j.getJobPosting().stream().filter(ju -> ju.getUserId().equals(userId))
                                                        .findFirst()
                                                        .ifPresentOrElse(jp -> {
                                                                String company = jp.getCompany() == null ? ""
                                                                                : jp.getCompany();
                                                                String position = jp.getPosition() == null ? ""
                                                                                : jp.getPosition();
                                                                String baseSalary = jp.getBaseSalary() == null ? ""
                                                                                : jp.getBaseSalary();
                                                                String commission = jp.getCommission() == null ? ""
                                                                                : jp.getCommission();
                                                                String workTime = jp.getWorkTime() == null ? ""
                                                                                : jp.getWorkTime();
                                                                String requirements = jp.getRequirements() == null ? ""
                                                                                : jp.getRequirements();
                                                                String location = jp.getLocation() == null ? ""
                                                                                : jp.getLocation();
                                                                String flightNumber = jp.getFlightNumber() == null ? ""
                                                                                : jp.getFlightNumber();

                                                                this.response.setText("招聘人才\n" +
                                                                                "公司：" + company + "\n" +
                                                                                "职位：" + position + "\n" +
                                                                                "底薪：" + baseSalary + "\n" +
                                                                                "提成：" + commission + "\n" +
                                                                                "上班时间：" + workTime + "\n" +
                                                                                "要求内容：" + requirements + "\n" +
                                                                                "🐌 地址：" + location + "\n" +
                                                                                "✈️咨询飞机号：" + flightNumber);

                                                                JobPostingDTO jobPostingDTO = new JobPostingDTO(userId,
                                                                                String.valueOf(id),
                                                                                company, position, baseSalary,
                                                                                commission, workTime, requirements,
                                                                                location, flightNumber);
                                                                this.response.setReplyMarkup(
                                                                                new KeyboardButton()
                                                                                                .keyboard_jobPosting(
                                                                                                                jobPostingDTO));
                                                                Integer lastMessageId = common
                                                                                .sendResponseAsync(this.response);
                                                                jobPosting.setLastMessageId(lastMessageId);
                                                                this.jobManagementServiceImpl
                                                                                .saveJobPosting(jobPosting);
                                                        }, () -> {
                                                                this.response.setText("招聘人才\n" +
                                                                                "公司：\n" +
                                                                                "职位：\n" +
                                                                                "底薪：\n" +
                                                                                "提成：\n" +
                                                                                "上班时间：\n" +
                                                                                "要求内容：\n" +
                                                                                "🐌 地址：\n" +
                                                                                "✈️咨询飞机号： ");
                                                                JobPostingDTO jobPostingDTO = new JobPostingDTO(userId,
                                                                                String.valueOf(common
                                                                                                .getSpringyBotId()));
                                                                this.response.setReplyMarkup(
                                                                                new KeyboardButton()
                                                                                                .keyboard_jobPosting(
                                                                                                                jobPostingDTO));
                                                                Integer lastMessageId = common
                                                                                .sendResponseAsync(this.response);
                                                                JobPosting jobPosting_ = new JobPosting(userId,
                                                                                String.valueOf(common
                                                                                                .getSpringyBotId()),
                                                                                lastMessageId);
                                                                springyBot.getJobUser().stream().filter(
                                                                                ju -> ju.getUserId().equals(userId))
                                                                                .findFirst()
                                                                                .ifPresent(ju -> ju.getJobPosting()
                                                                                                .add(jobPosting_));
                                                                springyBotServiceImpl.save(springyBot);
                                                        });
                                }, () -> {
                                        springyBot.getJobUser().add(jobUser);
                                        springyBotServiceImpl.save(springyBot);
                                });

        }

        private void sendTextWithJobSeeker(JobSeeker jobSeeker,Common common, RobotChannelManagement robotChannelManagement) {

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
                String result = sb.toString().trim(); // 去掉前后空格

                if (!result.isEmpty()) {
                        // 在此处使用RobotGroup对象进行操作
                        SendMessage response = new SendMessage();
                        String username = common.getUpdate().getMessage().getChat().getUserName();
                        response.setChatId(String.valueOf(robotChannelManagement.getChannelId()));
                        response.setText("求职人员\n\n" + result);
                        response.setReplyMarkup(new KeyboardButton().keyboard_callme(username));
                        common.sendResponseAsync(response);
                }

        }

        private void sendTextWithJobPosting(JobPosting jobPosting,Common common,RobotChannelManagement robotChannelManagement) {
                StringBuilder sb = new StringBuilder();
                appendIfNotEmpty(sb, "公司：", jobPosting.getCompany());
                appendIfNotEmpty(sb, "职位：", jobPosting.getPosition());
                appendIfNotEmpty(sb, "底薪：", jobPosting.getBaseSalary());
                appendIfNotEmpty(sb, "提成：", jobPosting.getCommission());
                appendIfNotEmpty(sb, "上班时间：", jobPosting.getWorkTime());
                appendIfNotEmpty(sb, "要求内容：", jobPosting.getRequirements());
                appendIfNotEmpty(sb, "🐌 地址：", jobPosting.getLocation());
                appendIfNotEmpty(sb, "✈️咨询飞机号：", jobPosting.getFlightNumber());
                String result = sb.toString().trim(); // 去掉前后空格

                if (!result.isEmpty()) {
                        SendMessage response = new SendMessage();
                        String username = common.getUpdate().getMessage().getChat().getUserName();
                        response.setChatId(String.valueOf(robotChannelManagement.getChannelId()));
                        response.setText("招聘人才\n\n" + result);
                        response.setReplyMarkup(new KeyboardButton().keyboard_callme(username));
                        common.sendResponseAsync(response);
                }
        }

        private void appendIfNotEmpty(StringBuilder sb, String label, String value) {
                if (value != null && !value.isEmpty()) {
                        sb.append(label).append(value).append("\n");
                }
        }

}
