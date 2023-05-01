package com.lwdevelop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;

@Repository
public interface ChannelMessageIdPostCountsRepository extends JpaRepository<ChannelMessageIdPostCounts, Long> {
    ChannelMessageIdPostCounts findByChannelIdAndType(Long channelId,String type);
    ChannelMessageIdPostCounts findByChannelIdAndUserIdAndType(Long channelId,String userId,String type);
    ChannelMessageIdPostCounts findByBotIdAndUserIdAndType(String botId,String userId,String type);
    List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndType(String botId,String userId,String type);
    
}
