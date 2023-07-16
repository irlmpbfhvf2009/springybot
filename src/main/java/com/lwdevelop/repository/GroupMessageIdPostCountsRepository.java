package com.lwdevelop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lwdevelop.entity.GroupMessageIdPostCounts;

@Repository
public interface GroupMessageIdPostCountsRepository extends JpaRepository<GroupMessageIdPostCounts, Long> {

    GroupMessageIdPostCounts findByBotIdAndUserIdAndType(String botId, String userId, String type);

    GroupMessageIdPostCounts findByGroupIdAndType(Long grouplId, String type);

    List<GroupMessageIdPostCounts> findAllByGroupIdAndType(Long groupId, String type);

    List<GroupMessageIdPostCounts> findAllByBotIdAndUserIdAndType(String botId, String userId, String type);
}
