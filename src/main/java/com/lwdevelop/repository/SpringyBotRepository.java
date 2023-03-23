package com.lwdevelop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lwdevelop.entity.SpringyBot;

public interface SpringyBotRepository extends JpaRepository<SpringyBot, Long> {
    SpringyBot findById(Integer id);
    Page<SpringyBot> findAllByUsernameContaining(String username,Pageable pageable);
    SpringyBot findByUsername(String username);
    SpringyBot findByToken(String token);
}
