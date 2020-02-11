package com.example.userservice.service;

import com.example.userservice.model.UserCredential;
import com.example.userservice.model.UserInfo;
import com.example.userservice.repository.UserCredentialRepo;
import com.example.userservice.repository.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Autowired
    private UserCredentialRepo userCredentialRepo;

    public boolean isUsernameAvailable(String username) {
        return !userInfoRepo.existsById(username);
    }

    public boolean isExistsByUsername(String username) {
        return userInfoRepo.existsById(username);
    }

    public Map<String, String> getMainInfo(String username) {
        Map<String, String> res = new HashMap<>();
        userInfoRepo.findById(username).ifPresent(userInfo -> {
            res.put("first_name", userInfo.getFirstName());
            res.put("last_name", userInfo.getLastName());
        });
        return res;
    }

    public boolean registerNewUser(String username, String passwordHash,
                                   String firstName, String lastName) {
        return userCredentialRepo.registerUser(username, passwordHash, firstName, lastName);
//
//        UserCredential userCredential = new UserCredential();
//        userCredential.setAuthorities("ROLE_USER");
//        userCredential.setUsername(username);
//        userCredential.setPasswordHash(passwordHash);
//
//        UserInfo userInfo = new UserInfo();
//        userInfo.setFirstName(firstName);
//        userInfo.setLastName(lastName);
//        userInfo.setUsername(username);
//        userInfo.setRegisterDate(new Date());
//
//        userInfoRepo.save(userInfo);
//        userCredentialRepo.save(userCredential);
//        return true;
    }
}
