package com.example.userservice.service;

import com.example.userservice.repository.UserCredentialRepo;
import com.example.userservice.repository.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserCredentialRepo userCredentialRepo;

    @Autowired
    private UserInfoRepo userInfoRepo;

    public boolean isExistsByUsername(String username) {
        return userCredentialRepo.existsById(username);
    }

    public String registerNewUser(String username, String email, String passwordHash,
                                   String firstName, String lastName) {
        if (userCredentialRepo.existsById(username)) {
            return "username";
        }
        if (userInfoRepo.existsByEmail(email)) {
            return "email";
        }
        userCredentialRepo.registerUser(username, email, passwordHash, firstName, lastName);
        return "ok";
    }
}
