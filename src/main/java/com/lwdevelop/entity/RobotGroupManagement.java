package com.lwdevelop.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class RobotGroupManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String groupId;

    private String groupTitle;

    private String link;

    @OneToOne(cascade = CascadeType.ALL)
    private SpringyBot springyBot;

}
