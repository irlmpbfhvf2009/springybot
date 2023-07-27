package org.springybot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 供應信息ID

    private String userId;

    private String botId;

    private String projectName; // 项目名称

    private String commission; // 佣金奖赏

    private String agency;  // 是否中介

    private String workTime; // 上班时间

    private String guarantee; // 是否支持担保

    private String projectDetail; // 项目内容

    private String flightNumber; // 咨询飞机号

    private String publisher;   //發布人

    private Integer lastMessageId;

    // channelId  messageId 訊息ID  postCount 發送次數
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ChannelMessageIdPostCounts> channelMessageIdPostCounts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GroupMessageIdPostCounts> groupMessageIdPostCounts;

    public Supply(String userId, String botId, Integer lastMessageId) {
        this.userId = userId;
        this.botId = botId;
        this.lastMessageId = lastMessageId;
    }
    
}
