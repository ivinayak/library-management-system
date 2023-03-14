package com.library.authorization.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private String token;

    private String type = "Bearer";

    private int userId;

    private String firstName;

    private String lastName;

    private String email;

    private String role;


    public JwtResponse(String jwt, int userId, String firstName, String lastName, String email, String role) {
        this.token = jwt;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
