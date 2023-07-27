package org.springybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springybot.entity.Config;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    
}
