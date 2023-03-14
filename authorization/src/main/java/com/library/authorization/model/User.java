package com.library.authorization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Integer userId;
    private String email;

    private String password;
    private String firstName;
    private String lastName;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roleId;

    @Transient
    private String role;

    public User(){}

    public User(Integer userId, String firstName, String lastName, String email, String encode) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = encode;
    }

    public String getRole(){
        if(null != this.getRoleId() && !StringUtils.isEmpty(this.getRoleId().getName())){
            return this.getRoleId().getName();
        }
        return this.role;
    }
}
