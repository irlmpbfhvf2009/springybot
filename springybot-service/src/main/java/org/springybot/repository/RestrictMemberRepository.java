package org.springybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springybot.entity.RestrictMember;

@Repository
public interface RestrictMemberRepository  extends JpaRepository<RestrictMember, Long> {
    
}
