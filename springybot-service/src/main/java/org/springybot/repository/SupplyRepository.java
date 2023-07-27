package org.springybot.repository;

import org.springybot.entity.Supply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {
    Supply findByUserIdAndBotId(String userId,String springyBotId);

    List<Supply> findAllByUserIdAndBotId(String userId, String springyBotId);

    
}
