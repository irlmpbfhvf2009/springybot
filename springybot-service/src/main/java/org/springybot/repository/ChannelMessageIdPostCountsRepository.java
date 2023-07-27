package org.springybot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springybot.entity.ChannelMessageIdPostCounts;

@Repository
public interface ChannelMessageIdPostCountsRepository extends JpaRepository<ChannelMessageIdPostCounts, Long> {
    ChannelMessageIdPostCounts findByChannelIdAndUserIdAndType(Long channelId,String userId,String type);
    List<ChannelMessageIdPostCounts> findAllByBotIdAndUserIdAndType(String botId,String userId,String type);
}
