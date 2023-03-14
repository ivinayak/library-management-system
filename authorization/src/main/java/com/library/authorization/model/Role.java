package com.library.authorization.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
}
