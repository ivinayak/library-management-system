package com.library.authorization.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.authorization.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(int userId, String firstName, String lastName, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user){
        List<GrantedAuthority> authorityList = Collections.singletonList(new SimpleGrantedAuthority(user.getRoleId().getName()));
        return new UserDetailsImpl(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), authorityList);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return firstName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
