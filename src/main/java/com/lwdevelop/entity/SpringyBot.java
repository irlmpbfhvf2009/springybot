package com.lwdevelop.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class SpringyBot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;
    
    private String username;

    private Boolean state;

    @OneToOne(cascade = CascadeType.ALL)
    private Config config;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private RobotGroupManagement robotGroupManagement;
}
