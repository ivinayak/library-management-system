package com.library.librarymanagement.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
public class Role {
    private Integer id;
    private String name;
    private String description;
}
