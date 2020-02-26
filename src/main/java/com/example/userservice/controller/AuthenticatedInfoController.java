package com.example.userservice.controller;

import com.example.userservice.model.UserInfo;
import com.example.userservice.repository.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("authenticated")
public class AuthenticatedInfoController {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @GetMapping("principal")
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

    @GetMapping("user-info")
    public UserInfo getUserInfo(OAuth2Authentication authenticated) {
        return userInfoRepo.findById(authenticated.getName()).get();
    }
}
