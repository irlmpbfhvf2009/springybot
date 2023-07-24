package com.lwdevelop.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lwdevelop.entity.JobPosting;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    JobPosting findByUserIdAndBotId(String userId, String springyBotId);

    List<JobPosting> findAllByUserIdAndBotId(String userId, String springyBotId);
}
