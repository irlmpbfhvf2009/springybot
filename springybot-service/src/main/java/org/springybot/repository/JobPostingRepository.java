package org.springybot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springybot.entity.JobPosting;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    JobPosting findByUserIdAndBotId(String userId, String springyBotId);

    List<JobPosting> findAllByUserIdAndBotId(String userId, String springyBotId);

    Optional<JobPosting> findAllByUserId(String userId);
}
