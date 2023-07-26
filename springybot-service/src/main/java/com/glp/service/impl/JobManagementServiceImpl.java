package com.glp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.glp.dto.JobPostingDTO;
import com.glp.dto.JobSeekerDTO;
import com.glp.dto.JobTreeDTO;
import com.glp.entity.ChannelMessageIdPostCounts;
import com.glp.entity.GroupMessageIdPostCounts;
import com.glp.entity.JobPosting;
import com.glp.entity.JobSeeker;
import com.glp.entity.TgUser;
import com.glp.entity.SpringyBot;
import com.glp.repository.ChannelMessageIdPostCountsRepository;
import com.glp.repository.GroupMessageIdPostCountsRepository;
import com.glp.repository.JobPostingRepository;
import com.glp.repository.JobSeekerRepository;
import com.glp.repository.TgUserRepository;
import com.glp.service.JobManagementService;
import com.glp.utils.ResponseUtils;
import com.glp.utils.RetEnum;
import com.glp.utils.ResponseUtils.ResponseData;

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

            List<TgUser> tgUsers = springyBotServiceImpl.findTgUserBySpringyBotId(springyBots.get(i).getId());
            for (int j = 0; j < tgUsers.size(); j++) {
                List<JobTreeDTO> jobTreeDTOList = new ArrayList<>();
                tgUsers.stream().forEach(tgUser -> {
                    JobTreeDTO jobTreeDTO = new JobTreeDTO();
                    jobTreeDTO.setId(tgUser.getId());
                    String name = tgUser.getUsername().equals("") ? tgUser.getFirstname() : tgUser.getUsername();
                    name = name.equals("") ? tgUser.getLastname() : name;
                    jobTreeDTO.setLabel(name);

                    Optional<JobPosting> jobPostingOptional = jobPostingRepository.findAllByUserId(tgUser.getUserId());
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

                    Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findAllByUserId(tgUser.getUserId());
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


}
