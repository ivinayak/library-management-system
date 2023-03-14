package com.library.librarymanagement.util;

import com.library.librarymanagement.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtil {


    private RestTemplate restTemplate = new RestTemplate();

    public User getUser(String email, String header){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", header);
        ResponseEntity<User> response = restTemplate.exchange(
                "http://localhost:8082/user/email?email=" + email,
                HttpMethod.GET,
                new HttpEntity<>("parameters", headers),
                User.class
        );

        return response.getBody();
    }
}
