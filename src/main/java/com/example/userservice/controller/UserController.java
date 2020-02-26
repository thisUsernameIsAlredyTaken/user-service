package com.example.userservice.controller;

import com.example.userservice.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("register")
    public ResponseEntity<Map<String, String>> registerNewUser(@RequestParam String username,
                                                               @RequestParam String email,
                                                               @RequestParam String firstName,
                                                               @RequestParam String lastName,
                                                               @RequestParam String passwordHash) {
        Map<String, String> map = new HashMap<>();
        String result = registerService.registerNewUser(username, email, passwordHash, firstName, lastName);
        if ("ok".equals(result)) {
            map.put("message", String.format("User \"%s\" created", username));
            return ResponseEntity.status(HttpStatus.CREATED).body(map);
        } else if ("username".equals(result)) {
            map.put("message", String.format("User \"%s\" exists", username));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
        } else {
            map.put("message", String.format("E-mail \"%s\" exists", email));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
        }
    }

    @GetMapping("exists-by-username/{username}")
    public ResponseEntity<Map<String, String>> checkIfUserExistByUsername(@PathVariable String username) {
        Map<String, String> map = new HashMap<>();
        if (registerService.isExistsByUsername(username)) {
            map.put("message", String.format("User \"%s\" exists", username));
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        map.put("message", String.format("User \"%s\" not found", username));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }
}
