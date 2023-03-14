package com.library.librarymanagement.security.service;

import com.library.librarymanagement.model.User;
import com.library.librarymanagement.security.jwt.JwtUtils;
import com.library.librarymanagement.util.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String authHeader) throws UsernameNotFoundException {
        String token = authHeader.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = restTemplateUtil.getUser(username, authHeader);
        return UserDetailsImpl.build(user);
    }
}
