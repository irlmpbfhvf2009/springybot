package org.springybot.repository;

import org.springybot.entity.Demand;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {
    Demand findByUserIdAndBotId(String userId,String springyBotId);

    List<Demand> findAllByUserIdAndBotId(String userId, String springyBotId);
    
}
