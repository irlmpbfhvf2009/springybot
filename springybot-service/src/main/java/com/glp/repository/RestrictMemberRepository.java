package com.glp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glp.entity.RestrictMember;

@Repository
public interface RestrictMemberRepository  extends JpaRepository<RestrictMember, Long> {
    
}
