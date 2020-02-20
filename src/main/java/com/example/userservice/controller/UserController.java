package com.example.userservice.controller;

import com.example.userservice.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("register")
    public ResponseEntity<String> registerNewUser(@RequestParam String username,
                                                  @RequestParam String firstName,
                                                  @RequestParam String lastName,
                                                  @RequestParam String passwordHash) {
        if (registerService.registerNewUser(username, passwordHash, firstName, lastName)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("User \"%s\" created", username));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("User \"%s\" exists", username));
        }
    }

    @GetMapping("exists-by-username/{username}")
    public ResponseEntity<String> checkIfUserExistByUsername(@PathVariable String username) {
        if (registerService.isExistsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    String.format("User \"%s\" exists", username)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                String.format("User \"%s\" not found", username)
        );
    }
}
