package org.springybot.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 關注頻道 (當followChannelSet時,用戶發言並且沒有訂閱頻道followChannelSet_chatId時,刪除訊息並發送系統消息(deleteSeconds秒後刪除系統消息))
    private Boolean followChannelSet; // 限制發言開關
    
    private Long followChannelSet_chatId; // 目標頻道id
    
    private String followChannelSet_chatTitle; // 目標頻道title
    
    private int deleteSeconds; // 系統消息刪除秒數
    
    // 邀請好友 (當inviteFriendsSet時,用戶邀請inviteFriendsQuantity人數後,才能發言)
    private Boolean inviteFriendsSet; // 限制發言開關

    private int inviteFriendsAutoClearTime; // 自動清除日期
    
    private int inviteFriendsQuantity; // 指定邀請人數
    
    // 邀請獎金 (當invitationBonusSet時,用戶邀請inviteMembers人數後,累積支付金額,系統消息提示 滿minimumPayout元聯繫contactPerson)
    private Boolean invitationBonusSet; // 系統開關

    private int inviteMembers; // 邀請目標人數

    private BigDecimal inviteEarnedOutstand; // 支付金額

    private BigDecimal minimumPayout; // 達到目標金額

    private String contactPerson; // 聯繫人

    private String password; // bot密碼

}
