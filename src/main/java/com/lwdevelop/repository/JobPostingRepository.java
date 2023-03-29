package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lwdevelop.entity.JobPosting;

public interface JobPostingRepository  extends JpaRepository<JobPosting, Long> {
    JobPosting findByUserId(String userId);
    
}
