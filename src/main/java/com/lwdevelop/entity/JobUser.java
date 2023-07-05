package com.lwdevelop.entity;

import java.util.Date;
import java.util.Set;
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
public class JobUser {

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
        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JsonIgnore
        private Set<JobSeeker> jobSeeker;

        // （招聘信息）
        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JsonIgnore
        private Set<JobPosting> jobPosting;

}
