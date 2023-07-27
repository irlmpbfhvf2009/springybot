package org.springybot.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Data;

/**
 * @ClassName RecordGroupUsers
 * @Description
 * @Author Leo
 * @Date 2023-6-20
 **/
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class RecordGroupUsers implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long botId;
    
    private Long userId;

    private String firstname;

    private String username;

    private String lastname;

    private Long groupId;

    private String groupTitle;

    private Boolean status;
    
    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;
    
}
