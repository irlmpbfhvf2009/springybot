package org.springybot.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 招聘信息ID

    private String userId;

    private String botId;

    private String company; // 公司名称

    private String position; // 职位名称

    private String baseSalary; // 底薪

    private String commission; // 提成

    private String nationality; // 国籍

    private String gender;  // 男女

    private String headCounts;   //  人數

    private String languages;   // 语言要求

    private String agency;  //是否中介

    private String workTime; // 上班时间

    private String requirements; // 要求内容

    private String location; // 地址

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

    public JobPosting(String userId, String botId, Integer lastMessageId) {
        this.userId = userId;
        this.botId = botId;
        this.lastMessageId = lastMessageId;
    }
    
}
