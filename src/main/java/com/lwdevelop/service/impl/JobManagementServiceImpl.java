package com.lwdevelop.service.impl;

import java.util.HashMap;

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
import com.lwdevelop.utils.CryptoUtil;
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
    public ResponseEntity<ResponseData> decryptedUbWithJobPosting(String ub) {
        String decryptedUb = CryptoUtil.decrypt(ub);
        HashMap<String, Object> data = new HashMap<>();
        String[] ubArray = decryptedUb.split("&");
        data.put("userId", ubArray[0].split("=")[1]);
        data.put("botId", ubArray[1].split("=")[1]);
        data.put("company", ubArray[2].split("=")[1]);
        data.put("position", ubArray[3].split("=")[1]);
        data.put("baseSalary", ubArray[4].split("=")[1]);
        data.put("commission", ubArray[5].split("=")[1]);
        data.put("workTime", ubArray[6].split("=")[1]);
        data.put("requirements", ubArray[7].split("=")[1]);
        data.put("location", ubArray[8].split("=")[1]);
        data.put("flightNumber", ubArray[9].split("=")[1]);
        return ResponseUtils.response(RetEnum.RET_SUCCESS,data);
    }

    @Override
    public ResponseEntity<ResponseData> addJobPosting(JobPostingDTO jobPostingDTO) {
        String decryptedUb = CryptoUtil.decrypt(jobPostingDTO.getUb());
        String[] ubArray = decryptedUb.split("&");
        String userId = ubArray[0].split("=")[1];
        String botId = ubArray[1].split("=")[1];
        Long id = Long.valueOf(botId);
        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        springyBot.getJobUser()
                .stream()
                .filter(jobUser -> jobUser.getUserId().equals(userId))
                .findFirst()
                .ifPresentOrElse(jobUser -> {
                    jobUser.getJobPosting()
                            .stream()
                            .filter(jp -> jp.getUserId().equals(userId))
                            .findFirst()
                            .ifPresentOrElse(oldJobPosting -> {
                                this.setJobPosting(oldJobPosting, jobPostingDTO, userId);
                            }, () -> {
                                JobPosting jobPosting = new JobPosting();
                                this.setJobPosting(jobPosting, jobPostingDTO, userId);
                                jobUser.getJobPosting().add(jobPosting);
                            });
                }, () -> {
                });
        springyBotServiceImpl.save(springyBot);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "編輯成功");
    }

    @Override
    public ResponseEntity<ResponseData> addJobSeeker(JobSeekerDTO jobSeekerDTO) {
        String decryptedUb = CryptoUtil.decrypt(jobSeekerDTO.getUb());
        String[] ubArray = decryptedUb.split("&");
        String userId = ubArray[0].split("=")[1];
        String botId = ubArray[1].split("=")[1];
        Long id = Long.valueOf(botId);

        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        springyBot.getJobUser()
                .stream()
                .filter(jobUser -> jobUser.getUserId().equals(userId))
                .findFirst()
                .ifPresentOrElse(jobUser -> {
                    jobUser.getJobSeeker()
                            .stream()
                            .filter(jobSeeker -> jobSeeker.getUserId().equals(userId))
                            .findFirst()
                            .ifPresentOrElse(oldJobSeeker -> {
                                this.setJobSeeker(oldJobSeeker, jobSeekerDTO, userId);
                            }, () -> {
                                JobSeeker jobSeeker = new JobSeeker();
                                this.setJobSeeker(jobSeeker, jobSeekerDTO, userId);
                                jobUser.getJobSeeker().add(jobSeeker);
                            });
                }, () -> {
                });
        springyBotServiceImpl.save(springyBot);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "編輯成功");
    }

    private void setJobPosting(JobPosting jobPosting, JobPostingDTO jobPostingDTO, String userId) {
        jobPosting.setUserId(userId);
        jobPosting.setBaseSalary(jobPostingDTO.getBaseSalary());
        jobPosting.setCommission(jobPostingDTO.getCommission());
        jobPosting.setCompany(jobPostingDTO.getCompany());
        jobPosting.setFlightNumber(jobPostingDTO.getFlightNumber());
        jobPosting.setLocation(jobPostingDTO.getLocation());
        jobPosting.setPosition(jobPostingDTO.getPosition());
        jobPosting.setRequirements(jobPostingDTO.getRequirements());
        jobPosting.setWorkTime(jobPostingDTO.getWorkTime());
    }

    private void setJobSeeker(JobSeeker jobSeeker, JobSeekerDTO jobSeekerDTO, String userId) {
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
        jobSeeker.setUserId(userId);
        jobSeeker.setWorkExperience(jobSeekerDTO.getWorkExperience());
    }

}
