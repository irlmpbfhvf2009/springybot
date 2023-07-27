package org.springybot.service.impl;

import org.springybot.entity.*;
import org.springybot.repository.*;
import org.springybot.service.DemandManagementService;
import org.springybot.utils.ResponseUtils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

// @Slf4j
@Service
public class DemandManagementServiceImpl implements DemandManagementService {

    // @Autowired
    // private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private ChannelMessageIdPostCountsRepository channelMessageIdPostCountsRepository;

    @Autowired
    private GroupMessageIdPostCountsRepository groupMessageIdPostCountsRepository;

    @Autowired
    private TgUserRepository tgUserRepository;

    @Override
    public void saveTgUser(TgUser tgUser) {
        tgUserRepository.save(tgUser);
    }

    @Override
    public ChannelMessageIdPostCounts findByChannelIdAndUserIdAndTypeWithChannelMessageIdPostCounts(Long channelId,
            String userId,
            String type) {
        return channelMessageIdPostCountsRepository.findByChannelIdAndUserIdAndType(channelId, userId, type);
    }

    @Override
    public GroupMessageIdPostCounts findByGroupIdAndUserIdAndTypeWithGroupMessageIdPostCounts(Long grouplId,
            String userId,
            String type) {
        return groupMessageIdPostCountsRepository.findByGroupIdAndUserIdAndType(grouplId, userId, type);
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
    public Demand findByUserIdAndBotIdWithDemand(String userId, String springyBotId) {
        return demandRepository.findByUserIdAndBotId(userId, springyBotId);
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
    public void saveChannelMessageIdPostCounts(ChannelMessageIdPostCounts channelMessageIdPostCounts) {
        channelMessageIdPostCountsRepository.save(channelMessageIdPostCounts);
    }

    @Override
    public void saveGroupMessageIdPostCounts(GroupMessageIdPostCounts groupMessageIdPostCounts) {
        groupMessageIdPostCountsRepository.save(groupMessageIdPostCounts);
    }

    @Override
    public List<Demand> findAllByUserIdAndBotIdWithDemand(String userId, String springyBotId) {
        return demandRepository.findAllByUserIdAndBotId(userId, springyBotId);
    }

    @Override
    public List<Supply> findAllByUserIdAndBotIdWithSupply(String userId, String springyBotId) {
        return supplyRepository.findAllByUserIdAndBotId(userId, springyBotId);
    }

    @Override
    public ResponseEntity<ResponseData> getDemandTreeData() {
        // List<JobTreeDTO> data = new ArrayList<>();
        // List<SpringyBot> springyBots = springyBotServiceImpl.findAll();
        //
        // for (int i = 0; i < springyBots.size(); i++) {
        // JobTreeDTO posting = new JobTreeDTO();
        // posting.setLabel("招聘信息");
        // posting.setId(0L);
        // JobTreeDTO seeker = new JobTreeDTO();
        // seeker.setLabel("求職信息");
        // seeker.setId(1L);
        //
        // List<JobUser> jobUsers =
        // springyBotServiceImpl.findJobUserBySpringyBotId(springyBots.get(i).getId());
        // for (int j = 0; j < jobUsers.size(); j++) {
        // List<JobTreeDTO> jobTreeDTOList = new ArrayList<>();
        // jobUsers.stream().forEach(jobUser -> {
        // JobTreeDTO jobTreeDTO = new JobTreeDTO();
        // jobTreeDTO.setId(jobUser.getId());
        // String name = jobUser.getUsername().equals("") ? jobUser.getFirstname() :
        // jobUser.getUsername();
        // name = name.equals("") ? jobUser.getLastname() : name;
        // jobTreeDTO.setLabel(name);
        //
        // Optional<JobPosting> jobPostingOptional = jobUser.getJobPosting().stream()
        // .filter(jp -> jp.getUserId().equals(jobUser.getUserId())).findAny();
        // if (jobPostingOptional.isPresent()) {
        // JobPosting jobPosting = jobPostingOptional.get();
        // JobPostingDTO jobPostingDTO = new JobPostingDTO();
        // jobPostingDTO.setBaseSalary(jobPosting.getBaseSalary());
        // jobPostingDTO.setBotId(jobPosting.getBotId());
        // jobPostingDTO.setCommission(jobPosting.getCommission());
        // jobPostingDTO.setCompany(jobPosting.getCompany());
        // jobPostingDTO.setFlightNumber(jobPosting.getFlightNumber());
        // jobPostingDTO.setId(jobPosting.getId());
        // jobPostingDTO.setLocation(jobPosting.getLocation());
        // jobPostingDTO.setPosition(jobPosting.getPosition());
        // jobPostingDTO.setRequirements(jobPosting.getRequirements());
        // jobPostingDTO.setUserId(jobPosting.getUserId());
        // jobPostingDTO.setWorkTime(jobPosting.getWorkTime());
        //
        // List<JobPostingDTO> list = new ArrayList<>();
        // list.add(jobPostingDTO);
        // jobTreeDTO.setJobPostingDTO(list);
        // }
        //
        // Optional<JobSeeker> jobSeekerOptional = jobUser.getJobSeeker().stream()
        // .filter(js -> js.getUserId().equals(jobUser.getUserId())).findAny();
        // if (jobSeekerOptional.isPresent()) {
        // JobSeeker jobSeeker = jobSeekerOptional.get();
        // JobSeekerDTO jobSeekerDTO = new JobSeekerDTO();
        // jobSeekerDTO.setAge(jobSeeker.getAge());
        // jobSeekerDTO.setBotId(jobSeeker.getBotId());
        // jobSeekerDTO.setDateOfBirth(jobSeeker.getDateOfBirth());
        // jobSeekerDTO.setEducation(jobSeeker.getEducation());
        // jobSeekerDTO.setExpectedSalary(jobSeeker.getExpectedSalary());
        // jobSeekerDTO.setFlightNumber(jobSeeker.getFlightNumber());
        // jobSeekerDTO.setGender(jobSeeker.getGender());
        // jobSeekerDTO.setId(jobSeeker.getId());
        // jobSeekerDTO.setName(jobSeeker.getName());
        // jobSeekerDTO.setNationality(jobSeeker.getNationality());
        // jobSeekerDTO.setResources(jobSeeker.getResources());
        // jobSeekerDTO.setSelfIntroduction(jobSeeker.getSelfIntroduction());
        // jobSeekerDTO.setSkills(jobSeeker.getSkills());
        // jobSeekerDTO.setTargetPosition(jobSeeker.getTargetPosition());
        // jobSeekerDTO.setUserId(jobSeeker.getUserId());
        // jobSeekerDTO.setWorkExperience(jobSeeker.getWorkExperience());
        //
        // List<JobSeekerDTO> list = new ArrayList<>();
        // list.add(jobSeekerDTO);
        // jobTreeDTO.setJobSeekerDTO(list);
        // }
        //
        // jobTreeDTOList.add(jobTreeDTO);
        // posting.setChildren(jobTreeDTOList);
        // seeker.setChildren(jobTreeDTOList);
        // });
        // }
        //
        // JobTreeDTO jobTreeDTO = new JobTreeDTO();
        // List<JobTreeDTO> children = new ArrayList<>();
        // children.add(seeker);
        // children.add(posting);
        //
        // jobTreeDTO.setLabel(springyBots.get(i).getUsername());
        // jobTreeDTO.setId((long) i);
        // jobTreeDTO.setChildren(children);
        // data.add(jobTreeDTO);
        // }
        // return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
        return null;
    }

}
