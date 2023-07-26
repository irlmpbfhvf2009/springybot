package com.lwdevelop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lwdevelop.entity.GroupMessageIdPostCounts;

@Repository
public interface GroupMessageIdPostCountsRepository extends JpaRepository<GroupMessageIdPostCounts, Long> {

    GroupMessageIdPostCounts findByGroupIdAndUserIdAndType(Long grouplId,String userId, String type);

    List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndType(String botId, String userId, String type);
}
