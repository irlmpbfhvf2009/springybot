package com.lwdevelop.repository;

import com.lwdevelop.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {
    Demand findByUserIdAndBotId(String userId,String springyBotId);    
    
}
