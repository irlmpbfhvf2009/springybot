package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lwdevelop.entity.JobSeeker;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    JobSeeker findByUserId(String userId);
    
    
}
