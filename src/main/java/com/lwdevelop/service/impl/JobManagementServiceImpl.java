package com.lwdevelop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.JobPostingRepository;
import com.lwdevelop.repository.JobSeekerRepository;
import com.lwdevelop.service.JobManagementService;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import com.lwdevelop.utils.ResponseUtils.ResponseData;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
@Service
public class JobManagementServiceImpl implements JobManagementService {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Override
    public JobSeeker findByUserIdWithJobSeeker(String userId) {
        return jobSeekerRepository.findByUserId(userId);
    }

    @Override
    public JobPosting findByUserIdWithJobPosting(String userId) {
        return jobPostingRepository.findByUserId(userId);
    }

    @Override
    public ResponseEntity<ResponseData> addJobPosting(JobPostingDTO jobPostingDTO) {
        Long id = Long.valueOf(jobPostingDTO.getBotId());
        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        springyBot.getJobUser()
                .stream()
                .filter(jobUser -> jobUser.getUserId().equals(jobPostingDTO.getUserId()))
                .findFirst()
                .ifPresentOrElse(jobUser -> {
                    jobUser.getJobPosting()
                            .stream()
                            .filter(jp -> jp.getUserId().equals(jobPostingDTO.getUserId()))
                            .findFirst()
                            .ifPresentOrElse(oldJobPosting -> {
                                this.setJobPosting(oldJobPosting,jobPostingDTO);
                            }, () -> {
                                JobPosting jobPosting = new JobPosting();
                                this.setJobPosting(jobPosting,jobPostingDTO);
                                jobUser.getJobPosting().add(jobPosting);
                            });
                }, () -> {
                });
        springyBotServiceImpl.save(springyBot);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "編輯成功");
    }

    @Override
    public ResponseEntity<ResponseData> addJobSeeker(JobSeekerDTO jobSeekerDTO) {
        Long id = Long.valueOf(jobSeekerDTO.getBotId());
        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        springyBot.getJobUser()
                .stream()
                .filter(jobUser -> jobUser.getUserId().equals(jobSeekerDTO.getUserId()))
                .findFirst()
                .ifPresentOrElse(jobUser -> {
                    jobUser.getJobSeeker()
                            .stream()
                            .filter(js -> js.getUserId().equals(jobSeekerDTO.getUserId()))
                            .findFirst()
                            .ifPresentOrElse(oldJobSeeker -> {
                                this.setJobSeeker(oldJobSeeker,jobSeekerDTO);
                            }, () -> {
                                JobSeeker jobSeeker = new JobSeeker();
                                this.setJobSeeker(jobSeeker,jobSeekerDTO);
                                jobUser.getJobSeeker().add(jobSeeker);
                            });
                }, () -> {
                });
        springyBotServiceImpl.save(springyBot);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "編輯成功");
    }

    private void setJobPosting(JobPosting jobPosting,JobPostingDTO jobPostingDTO){
        jobPosting.setUserId(jobPostingDTO.getUserId());
        jobPosting.setBaseSalary(jobPostingDTO.getBaseSalary());
        jobPosting.setCommission(jobPostingDTO.getCommission());
        jobPosting.setCompany(jobPostingDTO.getCompany());
        jobPosting.setFlightNumber(jobPostingDTO.getFlightNumber());
        jobPosting.setLocation(jobPostingDTO.getLocation());
        jobPosting.setPosition(jobPostingDTO.getPosition());
        jobPosting.setRequirements(jobPostingDTO.getRequirements());
        jobPosting.setWorkTime(jobPostingDTO.getWorkTime());
    }

    private void setJobSeeker(JobSeeker jobSeeker, JobSeekerDTO jobSeekerDTO) {
        jobSeeker.setAge(jobSeekerDTO.getAge());
        jobSeeker.setDateOfBirth(jobSeekerDTO.getDateOfBirth());
        jobSeeker.setEducation(jobSeekerDTO.getEducation());
        jobSeeker.setExpectedSalary(jobSeekerDTO.getExpectedSalary());
        jobSeeker.setGender(jobSeekerDTO.getGender());
        jobSeeker.setName(jobSeekerDTO.getName());
        jobSeeker.setNationality(jobSeekerDTO.getNationality());
        jobSeeker.setResources(jobSeekerDTO.getResources());
        jobSeeker.setSelfIntroduction(jobSeekerDTO.getSelfIntroduction());
        jobSeeker.setSkills(jobSeekerDTO.getSkills());
        jobSeeker.setTargetPosition(jobSeekerDTO.getTargetPosition());
        jobSeeker.setUserId(jobSeekerDTO.getUserId());
        jobSeeker.setWorkExperience(jobSeekerDTO.getWorkExperience());
    }

}
