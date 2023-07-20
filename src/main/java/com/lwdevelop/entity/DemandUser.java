package com.lwdevelop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DemandUser {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String userId;

        private String firstname;

        private String username;

        private String lastname;

        @CreatedDate
        private Date createdDate;

        // （供應信息）
        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JsonIgnore
        private Set<Supply> supply;

        // （需求信息）
        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JsonIgnore
        private Set<Demand> demand;

}
