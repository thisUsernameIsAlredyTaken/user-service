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
    public List<Map<String, Object>> getAllWatched(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   OAuth2Authentication authenticated) {
        return userFunctionsService.findAllWatchedByUsername(authenticated.getName(), page, pageSize);
    }

    @GetMapping("watched/{id}")
    public ResponseEntity<Map<String, Object>> getWatched(@PathVariable String id,
                                                          OAuth2Authentication authenticated) {
        Map<String, Object> movie = userFunctionsService.getWatchedById(authenticated.getName(), id);
        if (movie != null) {
            return ResponseEntity.status(HttpStatus.OK).body(movie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
    public List<Map<String, Object>> getAllPlanned(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   OAuth2Authentication authenticated) {
        return userFunctionsService.findAllPlannedByUsername(authenticated.getName(), page, pageSize);
    }

    @PatchMapping("watched")
    public ResponseEntity<Void> patchWatched(@RequestParam(required = false) Integer score,
                                             @RequestParam(required = false) String message,
                                             @RequestParam String movieId,
                                             OAuth2Authentication authenticated) {
        if (userFunctionsService.patchWatched(authenticated.getName(), movieId, score, message)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("planned/count")
    public Map<String, Integer> getPlannedCount(OAuth2Authentication authenticated) {
        Map<String, Integer> map = new HashMap<>();
        map.put("count", userFunctionsService.plannedCount(authenticated.getName()));
        return map;
    }

    @GetMapping("watched/count")
    public Map<String, Integer> getWatchedCount(OAuth2Authentication authenticated) {
        Map<String, Integer> map = new HashMap<>();
        map.put("count", userFunctionsService.watchedCount(authenticated.getName()));
        return map;
    }

    @DeleteMapping("listed/{movieId}")
    public void deleteListed(@PathVariable String movieId,
                             OAuth2Authentication authenticated) {
        userFunctionsService.deleteListed(authenticated.getName(), movieId);
    }

    @GetMapping("listed/{movieId}")
    public Map<String, String> getListedStatus(@PathVariable String movieId,
                                               OAuth2Authentication authenticated) {
        String s = userFunctionsService.listedStatus(authenticated.getName(), movieId);
        Map<String, String> map = new HashMap<>();
        if (s == null) {
            map.put("message", "none");
        } else {
            map.put("message", s);
        }
        return map;
    }

    @GetMapping("recommend")
    public List<Map<String, Object>> getRecommend(@RequestParam(defaultValue = "10") int count,
                                                  OAuth2Authentication authenticated) {
        return userFunctionsService.recommend(authenticated.getName(), count);
    }
}
