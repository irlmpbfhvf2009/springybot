package com.glp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.glp.entity.WhiteList;

@Repository
public interface WhiteListRepository extends JpaRepository<WhiteList, Long> {
    
    WhiteList findByUserId(Long userId);
    
}
