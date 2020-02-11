package com.example.userservice.controller;

import com.example.userservice.service.UserFunctionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("me")
public class UsersFunctionalController {

    @Autowired
    private UserFunctionalService userFunctionalService;

    @PostMapping("watched")
    public ResponseEntity<Void> addWatched(@RequestParam String movieId,
                                           @RequestParam(required = false) Integer score,
                                           @RequestParam(defaultValue = "") String message,
                                           OAuth2Authentication authenticated) {
        if (userFunctionalService.addWatched(authenticated.getName(), movieId, score, message)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("watched")
    public Map<String, Map<String, Object>> getAllWatched(OAuth2Authentication authenticated) {
        return userFunctionalService.findAllWatchedByUsername(authenticated.getName());
    }

    @PostMapping("planned")
    public ResponseEntity<Void> addPlanned(@RequestParam String movieId,
                                           OAuth2Authentication authenticated) {
        if (userFunctionalService.addPlanned(authenticated.getName(), movieId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("planned")
    public Map<String, Map<String, Object>> getAllPlanned(OAuth2Authentication authenticated) {
        return userFunctionalService.findAllPlannedByUsername(authenticated.getName());
    }
}
