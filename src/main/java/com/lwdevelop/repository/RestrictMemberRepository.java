package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lwdevelop.entity.RestrictMember;

@Repository
public interface RestrictMemberRepository  extends JpaRepository<RestrictMember, Long> {
    
}
