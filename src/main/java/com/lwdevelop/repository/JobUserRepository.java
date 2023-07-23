package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lwdevelop.entity.JobUser;

@Repository
public interface JobUserRepository extends JpaRepository<JobUser,Long>{
    
}
