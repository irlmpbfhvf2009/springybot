package com.lwdevelop.service.impl;

import com.lwdevelop.bot.bots.telent.TalentLongPollingBot;
import com.lwdevelop.bot.bots.utils.keyboardButton.TelentButton;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.JobTreeDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.*;
import com.lwdevelop.repository.*;
import com.lwdevelop.service.DemandManagementService;
import com.lwdevelop.service.JobManagementService;
import com.lwdevelop.utils.CryptoUtil;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.ResponseUtils.ResponseData;
import com.lwdevelop.utils.RetEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.ExecutionException;

// @Slf4j
@Service
public class DemandManagementServiceImpl implements DemandManagementService {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private ChannelMessageIdPostCountsRepository channelMessageIdPostCountsRepository;

    @Autowired
    private GroupMessageIdPostCountsRepository groupMessageIdPostCountsRepository;

    @Override
    public ChannelMessageIdPostCounts findByChannelIdAndTypeWithChannelMessageIdPostCounts(Long channelId,
            String type) {
        return channelMessageIdPostCountsRepository.findByChannelIdAndType(channelId, type);
    }

    @Override
    public ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,
            String userId, String type) {
        return channelMessageIdPostCountsRepository.findByChannelIdAndUserIdAndType(channelId, userId, type);
    }

    @Override
    public GroupMessageIdPostCounts findByGroupIdAndTypeWithGroupMessageIdPostCounts(Long grouplId,
            String type) {
        return groupMessageIdPostCountsRepository.findByGroupIdAndType(grouplId, type);
    }

    @Override
    public ChannelMessageIdPostCounts findByBotIdAndUserIdAndTypeWithChannelMessageIdPostCounts(String botId,
            String userId, String type) {
        return channelMessageIdPostCountsRepository.findByBotIdAndUserIdAndType(botId, userId, type);
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
    public Supply findByUserIdAndBotIdWithSupply(String userId, String springyBotId) {
        return supplyRepository.findByUserIdAndBotId(userId, springyBotId);
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
    public void deleteByIdChannelMessageIdPostCounts(Long id) {
        channelMessageIdPostCountsRepository.deleteById(id);
    }

    @Override
    public void deleteByIdGroupMessageIdPostCounts(Long id) {
        groupMessageIdPostCountsRepository.deleteById(id);
    }

    @Override
    public void saveDemand(Demand demand) {
        demandRepository.save(demand);
    }

    @Override
    public void saveSupply(Supply supply) {
        supplyRepository.save(supply);
    }

    @Override
    public Demand findByUserIdAndBotIdWithDemand(String userId, String springyBotId) {
        return demandRepository.findByUserIdAndBotId(userId, springyBotId);
    }

    @Override
    public void deleteByIdWithDemand(Long id) {
        demandRepository.deleteById(id);
    }


    @Override
    public ResponseEntity<ResponseData> getDemandTreeData() {
//        List<JobTreeDTO> data = new ArrayList<>();
//        List<SpringyBot> springyBots = springyBotServiceImpl.findAll();
//
//        for (int i = 0; i < springyBots.size(); i++) {
//            JobTreeDTO posting = new JobTreeDTO();
//            posting.setLabel("招聘信息");
//            posting.setId(0L);
//            JobTreeDTO seeker = new JobTreeDTO();
//            seeker.setLabel("求職信息");
//            seeker.setId(1L);
//
//            List<JobUser> jobUsers = springyBotServiceImpl.findJobUserBySpringyBotId(springyBots.get(i).getId());
//            for (int j = 0; j < jobUsers.size(); j++) {
//                List<JobTreeDTO> jobTreeDTOList = new ArrayList<>();
//                jobUsers.stream().forEach(jobUser -> {
//                    JobTreeDTO jobTreeDTO = new JobTreeDTO();
//                    jobTreeDTO.setId(jobUser.getId());
//                    String name = jobUser.getUsername().equals("") ? jobUser.getFirstname() : jobUser.getUsername();
//                    name = name.equals("") ? jobUser.getLastname() : name;
//                    jobTreeDTO.setLabel(name);
//
//                    Optional<JobPosting> jobPostingOptional = jobUser.getJobPosting().stream()
//                            .filter(jp -> jp.getUserId().equals(jobUser.getUserId())).findAny();
//                    if (jobPostingOptional.isPresent()) {
//                        JobPosting jobPosting = jobPostingOptional.get();
//                        JobPostingDTO jobPostingDTO = new JobPostingDTO();
//                        jobPostingDTO.setBaseSalary(jobPosting.getBaseSalary());
//                        jobPostingDTO.setBotId(jobPosting.getBotId());
//                        jobPostingDTO.setCommission(jobPosting.getCommission());
//                        jobPostingDTO.setCompany(jobPosting.getCompany());
//                        jobPostingDTO.setFlightNumber(jobPosting.getFlightNumber());
//                        jobPostingDTO.setId(jobPosting.getId());
//                        jobPostingDTO.setLocation(jobPosting.getLocation());
//                        jobPostingDTO.setPosition(jobPosting.getPosition());
//                        jobPostingDTO.setRequirements(jobPosting.getRequirements());
//                        jobPostingDTO.setUserId(jobPosting.getUserId());
//                        jobPostingDTO.setWorkTime(jobPosting.getWorkTime());
//
//                        List<JobPostingDTO> list = new ArrayList<>();
//                        list.add(jobPostingDTO);
//                        jobTreeDTO.setJobPostingDTO(list);
//                    }
//
//                    Optional<JobSeeker> jobSeekerOptional = jobUser.getJobSeeker().stream()
//                            .filter(js -> js.getUserId().equals(jobUser.getUserId())).findAny();
//                    if (jobSeekerOptional.isPresent()) {
//                        JobSeeker jobSeeker = jobSeekerOptional.get();
//                        JobSeekerDTO jobSeekerDTO = new JobSeekerDTO();
//                        jobSeekerDTO.setAge(jobSeeker.getAge());
//                        jobSeekerDTO.setBotId(jobSeeker.getBotId());
//                        jobSeekerDTO.setDateOfBirth(jobSeeker.getDateOfBirth());
//                        jobSeekerDTO.setEducation(jobSeeker.getEducation());
//                        jobSeekerDTO.setExpectedSalary(jobSeeker.getExpectedSalary());
//                        jobSeekerDTO.setFlightNumber(jobSeeker.getFlightNumber());
//                        jobSeekerDTO.setGender(jobSeeker.getGender());
//                        jobSeekerDTO.setId(jobSeeker.getId());
//                        jobSeekerDTO.setName(jobSeeker.getName());
//                        jobSeekerDTO.setNationality(jobSeeker.getNationality());
//                        jobSeekerDTO.setResources(jobSeeker.getResources());
//                        jobSeekerDTO.setSelfIntroduction(jobSeeker.getSelfIntroduction());
//                        jobSeekerDTO.setSkills(jobSeeker.getSkills());
//                        jobSeekerDTO.setTargetPosition(jobSeeker.getTargetPosition());
//                        jobSeekerDTO.setUserId(jobSeeker.getUserId());
//                        jobSeekerDTO.setWorkExperience(jobSeeker.getWorkExperience());
//
//                        List<JobSeekerDTO> list = new ArrayList<>();
//                        list.add(jobSeekerDTO);
//                        jobTreeDTO.setJobSeekerDTO(list);
//                    }
//
//                    jobTreeDTOList.add(jobTreeDTO);
//                    posting.setChildren(jobTreeDTOList);
//                    seeker.setChildren(jobTreeDTOList);
//                });
//            }
//
//            JobTreeDTO jobTreeDTO = new JobTreeDTO();
//            List<JobTreeDTO> children = new ArrayList<>();
//            children.add(seeker);
//            children.add(posting);
//
//            jobTreeDTO.setLabel(springyBots.get(i).getUsername());
//            jobTreeDTO.setId((long) i);
//            jobTreeDTO.setChildren(children);
//            data.add(jobTreeDTO);
//        }
//        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
        return null;
    }



}
