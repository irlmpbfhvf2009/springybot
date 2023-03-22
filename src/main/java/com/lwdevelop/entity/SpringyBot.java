package com.lwdevelop.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "springyBot")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<RobotGroupManagement> robotGroupManagement;
}
