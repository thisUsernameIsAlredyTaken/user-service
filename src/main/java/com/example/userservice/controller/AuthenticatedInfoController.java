package com.example.userservice.controller;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("authenticated/principal")
public class AuthenticatedInfoController {

    @GetMapping
    public Map<String, Object> getAuthenticatedInfo(OAuth2Authentication user) {
        HashMap<String, Object> info = new HashMap<>();
        if (user == null) {
            return info;
        }
        info.put("username", user.getName());
        info.put("user", user.getUserAuthentication().getPrincipal());
        info.put("authorities", user.getUserAuthentication().getAuthorities());
        return info;
    }
}
