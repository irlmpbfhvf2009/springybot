package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lwdevelop.entity.TgUser;

@Repository
public interface TgUserRepository extends JpaRepository<TgUser,Long>{
    
}
