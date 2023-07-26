package com.glp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.glp.entity.Config;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    
}
