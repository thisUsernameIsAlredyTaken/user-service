package com.example.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("movieservice")
public interface MovieServiceFeign {

    @GetMapping("/info/{movieId}")
    Map<String, Object> findMovieById(@PathVariable String movieId);

    @GetMapping("/info")
    Map<String, Map<String, Object>> findMoviesByIds(@RequestParam List<String> movieIds);
}
