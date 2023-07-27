package org.springybot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springybot.entity.JobSeeker;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    JobSeeker findByUserIdAndBotId(String userId, String springyBotId);

    List<JobSeeker> findAllByUserIdAndBotId(String userId, String springyBotId);

    Optional<JobSeeker> findAllByUserId(String userId);
}
