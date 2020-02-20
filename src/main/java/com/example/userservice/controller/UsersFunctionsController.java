package com.example.userservice.controller;

import com.example.userservice.service.UserFunctionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("me")
public class UsersFunctionsController {

    @Autowired
    private UserFunctionsService userFunctionsService;

    @PostMapping("watched")
    public ResponseEntity<Void> addWatched(@RequestParam String movieId,
                                           @RequestParam(required = false) Integer score,
                                           @RequestParam(defaultValue = "") String message,
                                           OAuth2Authentication authenticated) {
        if (userFunctionsService.addWatched(authenticated.getName(), movieId, score, message)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("watched")
    public List<Map<String, Object>> getAllWatched(OAuth2Authentication authenticated) {
        return userFunctionsService.findAllWatchedByUsername(authenticated.getName());
    }

    @PostMapping("planned")
    public ResponseEntity<Void> addPlanned(@RequestParam String movieId,
                                           OAuth2Authentication authenticated) {
        if (userFunctionsService.addPlanned(authenticated.getName(), movieId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("planned")
    public List<Map<String, Object>> getAllPlanned(OAuth2Authentication authenticated) {
        return userFunctionsService.findAllPlannedByUsername(authenticated.getName());
    }

    @DeleteMapping("listed/{movieId}")
    public void deleteListed(@PathVariable String movieId,
                             OAuth2Authentication authenticated) {
        userFunctionsService.deleteListed(authenticated.getName(), movieId);
    }

    @GetMapping("listed/{movieId}")
    public ResponseEntity<Map<String, String>> getListedStatus(@PathVariable String movieId,
                                                               OAuth2Authentication authenticated) {
        String s = userFunctionsService.listedStatus(authenticated.getName(), movieId);
        Map<String, String> map = new HashMap<>();
        map.put("listed", s);
        if (s == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
    }

    @GetMapping("recommend")
    public List<Map<String, Object>> getRecommend(OAuth2Authentication authenticated) {
        return userFunctionsService.recommend(authenticated.getName());
    }
}
