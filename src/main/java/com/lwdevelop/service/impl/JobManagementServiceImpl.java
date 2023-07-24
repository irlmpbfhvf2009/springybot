package com.lwdevelop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.bots.talent.TalentLongPollingBot;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.JobTreeDTO;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;
import com.lwdevelop.entity.GroupMessageIdPostCounts;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.JobUser;
import com.lwdevelop.entity.RobotChannelManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.ChannelMessageIdPostCountsRepository;
import com.lwdevelop.repository.GroupMessageIdPostCountsRepository;
import com.lwdevelop.repository.JobPostingRepository;
import com.lwdevelop.repository.JobSeekerRepository;
import com.lwdevelop.repository.JobUserRepository;
import com.lwdevelop.service.JobManagementService;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import com.lwdevelop.utils.ResponseUtils.ResponseData;

// @Slf4j
@Service
public class JobManagementServiceImpl implements JobManagementService {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private ChannelMessageIdPostCountsRepository channelMessageIdPostCountsRepository;

    @Autowired
    private GroupMessageIdPostCountsRepository groupMessageIdPostCountsRepository;

    @Autowired
    private JobUserRepository jobUserRepository;

    @Override
    public void saveJobUser(JobUser jobUser) {
        jobUserRepository.save(jobUser);
    }

    @Override
    public ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,
            String userId,
            String type) {
        return channelMessageIdPostCountsRepository.findByChannelIdAndUserIdAndType(channelId, userId, type);
    }

    @Override
    public GroupMessageIdPostCounts findByGroupIdAndUserIdAndTypeWithGroupMessageIdPostCounts(Long grouplId, String userId,
            String type) {
        return groupMessageIdPostCountsRepository.findByGroupIdAndUserIdAndType(grouplId,userId, type);
    }

    @Override
    public List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,
            String userId, String type) {
        return channelMessageIdPostCountsRepository.findAllByBotIdAndUserIdAndType(botId, userId, type);
    }

    @Override
    public List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndTypeWithGroupMessageIdPostCounts(String botId,
            String userId, String type) {
        return groupMessageIdPostCountsRepository.findAllByBotIdAndUserIdAndType(botId, userId, type);
    }

    @Override
    public JobSeeker findByUserIdAndBotIdWithJobSeeker(String userId, String springyBotId) {
        return jobSeekerRepository.findByUserIdAndBotId(userId, springyBotId);
    }

    @Override
    public List<JobPosting> findAllByUserIdAndBotIdWithJobPosting(String userId, String springyBotId) {
        return jobPostingRepository.findAllByUserIdAndBotId(userId, springyBotId);
    }
    @Override
    public List<JobSeeker> findAllByUserIdAndBotIdWithJobSeeker(String userId, String springyBotId) {
        return jobSeekerRepository.findAllByUserIdAndBotId(userId, springyBotId);
    }

    @Override
    public void saveChannelMessageIdPostCounts(ChannelMessageIdPostCounts channelMessageIdPostCounts) {
        channelMessageIdPostCountsRepository.save(channelMessageIdPostCounts);
    }

    @Override
    public void saveGroupMessageIdPostCounts(GroupMessageIdPostCounts groupMessageIdPostCounts) {
        groupMessageIdPostCountsRepository.save(groupMessageIdPostCounts);
    }

    @Override
    public void saveJobPosting(JobPosting jobPosting) {
        jobPostingRepository.save(jobPosting);
    }

    @Override
    public void saveJobSeeker(JobSeeker jobsSeeker) {
        jobSeekerRepository.save(jobsSeeker);
    }

    @Override
    public JobPosting findByUserIdAndBotIdWithJobPosting(String userId, String springyBotId) {
        return jobPostingRepository.findByUserIdAndBotId(userId, springyBotId);
    }

    @Override
    public ResponseEntity<ResponseData> getJobTreeData() {
        List<JobTreeDTO> data = new ArrayList<>();
        List<SpringyBot> springyBots = springyBotServiceImpl.findAll();

        for (int i = 0; i < springyBots.size(); i++) {
            JobTreeDTO posting = new JobTreeDTO();
            posting.setLabel("招聘信息");
            posting.setId(0L);
            JobTreeDTO seeker = new JobTreeDTO();
            seeker.setLabel("求職信息");
            seeker.setId(1L);

            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(springyBots.get(i).getId());
            for (int j = 0; j < jobUsers.size(); j++) {
                List<JobTreeDTO> jobTreeDTOList = new ArrayList<>();
                jobUsers.stream().forEach(jobUser -> {
                    JobTreeDTO jobTreeDTO = new JobTreeDTO();
                    jobTreeDTO.setId(jobUser.getId());
                    String name = jobUser.getUsername().equals("") ? jobUser.getFirstname() : jobUser.getUsername();
                    name = name.equals("") ? jobUser.getLastname() : name;
                    jobTreeDTO.setLabel(name);

                    Optional<JobPosting> jobPostingOptional = jobUser.getJobPosting().stream()
                            .filter(jp -> jp.getUserId().equals(jobUser.getUserId())).findAny();
                    if (jobPostingOptional.isPresent()) {
                        JobPosting jobPosting = jobPostingOptional.get();
                        JobPostingDTO jobPostingDTO = new JobPostingDTO();
                        jobPostingDTO.setBaseSalary(jobPosting.getBaseSalary());
                        jobPostingDTO.setBotId(jobPosting.getBotId());
                        jobPostingDTO.setCommission(jobPosting.getCommission());
                        jobPostingDTO.setCompany(jobPosting.getCompany());
                        jobPostingDTO.setFlightNumber(jobPosting.getFlightNumber());
                        jobPostingDTO.setId(jobPosting.getId());
                        jobPostingDTO.setLocation(jobPosting.getLocation());
                        jobPostingDTO.setPosition(jobPosting.getPosition());
                        jobPostingDTO.setRequirements(jobPosting.getRequirements());
                        jobPostingDTO.setUserId(jobPosting.getUserId());
                        jobPostingDTO.setWorkTime(jobPosting.getWorkTime());

                        List<JobPostingDTO> list = new ArrayList<>();
                        list.add(jobPostingDTO);
                        jobTreeDTO.setJobPostingDTO(list);
                    }

                    Optional<JobSeeker> jobSeekerOptional = jobUser.getJobSeeker().stream()
                            .filter(js -> js.getUserId().equals(jobUser.getUserId())).findAny();
                    if (jobSeekerOptional.isPresent()) {
                        JobSeeker jobSeeker = jobSeekerOptional.get();
                        JobSeekerDTO jobSeekerDTO = new JobSeekerDTO();
                        jobSeekerDTO.setAge(jobSeeker.getAge());
                        jobSeekerDTO.setBotId(jobSeeker.getBotId());
                        jobSeekerDTO.setDateOfBirth(jobSeeker.getDateOfBirth());
                        jobSeekerDTO.setEducation(jobSeeker.getEducation());
                        jobSeekerDTO.setExpectedSalary(jobSeeker.getExpectedSalary());
                        jobSeekerDTO.setFlightNumber(jobSeeker.getFlightNumber());
                        jobSeekerDTO.setGender(jobSeeker.getGender());
                        jobSeekerDTO.setId(jobSeeker.getId());
                        jobSeekerDTO.setName(jobSeeker.getName());
                        jobSeekerDTO.setNationality(jobSeeker.getNationality());
                        jobSeekerDTO.setResources(jobSeeker.getResources());
                        jobSeekerDTO.setSelfIntroduction(jobSeeker.getSelfIntroduction());
                        jobSeekerDTO.setSkills(jobSeeker.getSkills());
                        jobSeekerDTO.setTargetPosition(jobSeeker.getTargetPosition());
                        jobSeekerDTO.setUserId(jobSeeker.getUserId());
                        jobSeekerDTO.setWorkExperience(jobSeeker.getWorkExperience());

                        List<JobSeekerDTO> list = new ArrayList<>();
                        list.add(jobSeekerDTO);
                        jobTreeDTO.setJobSeekerDTO(list);
                    }

                    jobTreeDTOList.add(jobTreeDTO);
                    posting.setChildren(jobTreeDTOList);
                    seeker.setChildren(jobTreeDTOList);
                });
            }

            JobTreeDTO jobTreeDTO = new JobTreeDTO();
            List<JobTreeDTO> children = new ArrayList<>();
            children.add(seeker);
            children.add(posting);

            jobTreeDTO.setLabel(springyBots.get(i).getUsername());
            jobTreeDTO.setId((long) i);
            jobTreeDTO.setChildren(children);
            data.add(jobTreeDTO);
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    public void sendTextWithJobPosting(JobPosting jobPosting, TalentLongPollingBot custom,
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
        Long channelId = robotChannelManagement.getChannelId();
        String channelTitle = robotChannelManagement.getChannelTitle();
        String channelLink = robotChannelManagement.getLink();

        if (!result.isEmpty()) {
            response.setChatId(channelId.toString());
            response.setText("招聘人才\n\n" + result);
            try {
                ChannelMessageIdPostCounts channelMessageIdPostCounts = findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(
                        channelId,jobPosting.getUserId(), "jobPosting");

                if (channelMessageIdPostCounts == null) {
                    final Integer channelMessageId = custom.executeAsync(response).get().getMessageId();
                    channelMessageIdPostCounts = new ChannelMessageIdPostCounts();
                    channelMessageIdPostCounts.setBotId(jobPosting.getBotId());
                    channelMessageIdPostCounts.setUserId(jobPosting.getUserId());
                    channelMessageIdPostCounts.setChannelId(channelId);
                    channelMessageIdPostCounts.setChannelTitle(channelTitle);
                    channelMessageIdPostCounts.setChannelLink(channelLink);
                    channelMessageIdPostCounts.setMessageId(channelMessageId);
                    channelMessageIdPostCounts.setPostCount(1);
                    channelMessageIdPostCounts.setType("jobPosting");
                    jobPosting.getChannelMessageIdPostCounts().add(channelMessageIdPostCounts);
                    this.saveJobPosting(jobPosting);
                } else {
                    if (channelMessageIdPostCounts.getPostCount() == 0) {
                        final Integer channelMessageId = custom.executeAsync(response).get().getMessageId();
                        channelMessageIdPostCounts.setMessageId(channelMessageId);
                        channelMessageIdPostCounts.setPostCount(channelMessageIdPostCounts.getPostCount() + 1);
                        this.saveChannelMessageIdPostCounts(channelMessageIdPostCounts);
                    } else {
                        response = new SendMessage();
                        response.setChatId(jobPosting.getUserId());
                        response.setText("用户只能发布一条[招聘人才]信息");
                        custom.executeAsync(response);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

    private void appendIfNotEmpty(StringBuilder sb, String label, String value) {
        if (value != null && !value.isEmpty()) {
            sb.append(label).append(value).append("\n");
        }
    }

}
