package org.springybot.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TgUser {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String userId;

        private String firstname;

        private String username;

        private String lastname;

        @CreatedDate
        private Date createdDate;

        // （求职人员）
        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonIgnore
        private List<JobSeeker> jobSeeker;

        // （招聘信息）
        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonIgnore
        private List<JobPosting> jobPosting;

        // （供應信息）
        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonIgnore
        private List<Supply> supply;

        // （需求信息）
        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonIgnore
        private List<Demand> demand;
}
