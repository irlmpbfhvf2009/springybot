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
        JobPosting jobPosting = new JobPosting();
        jobPosting.setBaseSalary(jobPostingDTO.getBaseSalary());
        jobPosting.setCommission(jobPostingDTO.getCommission());
        jobPosting.setCompany(jobPostingDTO.getCompany());
        jobPosting.setFlightNumber(jobPostingDTO.getFlightNumber());
        jobPosting.setLocation(jobPostingDTO.getLocation());
        jobPosting.setPosition(jobPostingDTO.getPosition());
        jobPosting.setRequirements(jobPostingDTO.getRequirements());
        jobPosting.setWorkTime(jobPostingDTO.getWorkTime());

        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        springyBot.getJobUser()
                .stream()
                .filter(jobUser -> jobUser.getUserId().equals(jobPostingDTO.getUserId()))
                .findFirst()
                .ifPresentOrElse(jobUser -> {
                    jobUser.getJobPosting().stream().filter(jp -> jp.getUserId().equals(jobPostingDTO.getUserId()))
                            .findFirst()
                            .ifPresentOrElse(oldJobPosting -> {
                                oldJobPosting.setBaseSalary(jobPostingDTO.getBaseSalary());
                                oldJobPosting.setCommission(jobPostingDTO.getCommission());
                                oldJobPosting.setCompany(jobPostingDTO.getCompany());
                                oldJobPosting.setFlightNumber(jobPostingDTO.getFlightNumber());
                                oldJobPosting.setLocation(jobPostingDTO.getLocation());
                                oldJobPosting.setPosition(jobPostingDTO.getPosition());
                                oldJobPosting.setRequirements(jobPostingDTO.getRequirements());
                                oldJobPosting.setUserId(jobPostingDTO.getUserId());
                                oldJobPosting.setWorkTime(jobPostingDTO.getWorkTime());
                            }, () -> jobUser.getJobPosting().add(jobPosting));
                }, () -> {
                });
        springyBotServiceImpl.save(springyBot);

        System.out.println(jobPostingDTO);
        return null;
    }

    @Override
    public ResponseEntity<ResponseData> addJobSeeker(JobSeekerDTO jobSeekerDTO) {
        return null;
    }

}
