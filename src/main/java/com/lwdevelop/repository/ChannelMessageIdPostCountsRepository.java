package com.lwdevelop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lwdevelop.entity.ChannelMessageIdPostCounts;

@Repository
public interface ChannelMessageIdPostCountsRepository extends JpaRepository<ChannelMessageIdPostCounts, Long> {
    ChannelMessageIdPostCounts findByChannelId(Long channelId);
    ChannelMessageIdPostCounts findByType(String type);
    ChannelMessageIdPostCounts findByChannelIdAndType(Long channelId,String type);
    
}
