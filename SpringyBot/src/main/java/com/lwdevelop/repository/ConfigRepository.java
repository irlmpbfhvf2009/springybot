package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lwdevelop.entity.Config;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    
}
