package com.lwdevelop.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="springyBot_id")
    private SpringyBot springyBot;

}
