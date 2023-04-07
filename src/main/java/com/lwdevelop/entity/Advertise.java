package com.lwdevelop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;


/**
 * 广告实体类
 */
    @Data
    @Entity
        public class Advertise {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private Long botId;

        private String contact;

        private int deilyTime;

        private String path;

    @ManyToMany(cascade =  CascadeType.REFRESH)
    @JsonIgnore
    private Set<RobotGroupManagement> robotGroupManagement;
    }

