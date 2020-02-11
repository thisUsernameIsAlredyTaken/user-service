package com.example.userservice.service;

import com.example.userservice.repository.UserCredentialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserCredentialRepo userCredentialRepo;

    public boolean isExistsByUsername(String username) {
        return userCredentialRepo.existsById(username);
    }

    public boolean registerNewUser(String username, String passwordHash,
                                   String firstName, String lastName) {
        return userCredentialRepo.registerUser(username, passwordHash, firstName, lastName);
    }
}
