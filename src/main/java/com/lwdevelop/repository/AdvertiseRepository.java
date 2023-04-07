package com.lwdevelop.repository;

import com.lwdevelop.entity.Advertise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertiseRepository extends JpaRepository<Advertise,Long> {

    Advertise findAdvertiseById(Long id);
    Page<Advertise> findAllByContactContaining(String contact, Pageable pageable);
    Advertise findByContact(String username);

    List<Advertise> findByBotIdLike(Long botId);



}
