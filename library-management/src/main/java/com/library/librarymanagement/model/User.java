package com.library.librarymanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
public class User {

    private Integer userId;
    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;

    private Role roleId;

    private String role;

    public User(){}

    public User(Integer userId, String firstName, String lastName, String email, String encode) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = encode;
    }
}
