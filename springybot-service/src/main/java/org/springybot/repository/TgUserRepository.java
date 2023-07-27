package org.springybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springybot.entity.TgUser;

@Repository
public interface TgUserRepository extends JpaRepository<TgUser,Long>{
    
}
