package com.example.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("movieservice")
public interface MovieServiceFeign {

    @GetMapping("/info/{movieId}")
    Map<String, Object> findMovieById(@PathVariable String movieId);

    @GetMapping("/info")
    List<Map<String, Object>> findMoviesByIds(@RequestParam List<String> movieIds);

    @GetMapping("/recommend")
    List<Map<String, Object>> recommend(@RequestParam String watched,
                                        @RequestParam String scores,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize);
}
