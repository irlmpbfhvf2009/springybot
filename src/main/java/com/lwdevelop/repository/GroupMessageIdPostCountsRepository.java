package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lwdevelop.entity.GroupMessageIdPostCounts;

@Repository
public interface GroupMessageIdPostCountsRepository extends JpaRepository<GroupMessageIdPostCounts, Long> {
    
    GroupMessageIdPostCounts findByGroupIdAndType(Long grouplId,String type);
}
