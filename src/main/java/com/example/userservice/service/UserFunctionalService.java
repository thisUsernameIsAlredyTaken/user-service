package com.example.userservice.service;

import com.example.userservice.feign.MovieServiceFeign;
import com.example.userservice.model.PlannedMovie;
import com.example.userservice.model.WatchedMovie;
import com.example.userservice.repository.PlannedMovieRepo;
import com.example.userservice.repository.WatchedMovieRepo;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class UserFunctionalService {

    @Autowired
    private WatchedMovieRepo watchedMovieRepo;

    @Autowired
    private PlannedMovieRepo plannedMovieRepo;

    @Autowired
    private MovieServiceFeign movieServiceFeign;

    public boolean addWatched(String username, String movieId, Integer score, String message) {
        try {
            movieServiceFeign.findMovieById(movieId);
        } catch (FeignException e) {
            if (e.status() == HttpServletResponse.SC_NOT_FOUND) {
                return false;
            } else {
                throw e;
            }
        }
        WatchedMovie wm = new WatchedMovie();
        wm.setDate(new Date());
        wm.setMovieId(movieId);
        wm.setMessage(message);
        wm.setRating(score);
        wm.setUsername(username);
        watchedMovieRepo.save(wm);
        return true;
    }

    public boolean addPlanned(String username, String movieId) {
        try {
            movieServiceFeign.findMovieById(movieId);
        } catch (FeignException e) {
            if (e.status() == HttpServletResponse.SC_NOT_FOUND) {
                return false;
            } else {
                throw e;
            }
        }
        PlannedMovie pm = new PlannedMovie();
        pm.setDate(new Date());
        pm.setUsername(username);
        pm.setMovieId(movieId);
        plannedMovieRepo.save(pm);
        return true;
    }

    private Map<String, Map<String, Object>> mergeInfoPlanned(List<PlannedMovie> plannedMovies,
                                                              Map<String, Map<String, Object>> moviesMap) {
        Map<String, Map<String, Object>> res = new HashMap<>();
        for (PlannedMovie plannedMovie : plannedMovies) {
            String id = plannedMovie.getMovieId();
            Map<String, Object> movieObj = moviesMap.get(id);
            if (movieObj != null) {
                Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put("date", plannedMovie.getDate());
                res.put("additional_info", additionalInfo);
            }
        }
        return res;
    }

    private Map<String, Map<String, Object>> mergeInfoWatched(List<WatchedMovie> watchedMovies,
                                                              Map<String, Map<String, Object>> moviesMap) {
        Map<String, Map<String, Object>> res = new HashMap<>();
        for (WatchedMovie watchedMovie : watchedMovies) {
            String id = watchedMovie.getMovieId();
            Map<String, Object> movieObj = moviesMap.get(id);
            if (movieObj != null) {
                Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put("score", watchedMovie.getRating());
                additionalInfo.put("message", watchedMovie.getMessage());
                additionalInfo.put("date", watchedMovie.getDate());
                movieObj.put("additional_info", additionalInfo);
                res.put(id, movieObj);
            }
        }
        return res;
    }

    public Map<String, Map<String, Object>> findAllPlannedByUsername(String username) {
        List<PlannedMovie> plannedMovies = plannedMovieRepo.findAllByUsername(username);
        List<String> plannedMoviesIds = new ArrayList<>();
        for (PlannedMovie plannedMovie : plannedMovies) {
            plannedMoviesIds.add(plannedMovie.getMovieId());
        }
        return mergeInfoPlanned(plannedMovies, movieServiceFeign.findMoviesByIds(plannedMoviesIds));
    }

    public Map<String, Map<String, Object>> findAllWatchedByUsername(String username) {
        List<WatchedMovie> watchedMovies = watchedMovieRepo.findAllByUsername(username);
        List<String> watchedMovieIds = new ArrayList<>();
        for (WatchedMovie watchedMovie : watchedMovies) {
            watchedMovieIds.add(watchedMovie.getMovieId());
        }
        return mergeInfoWatched(watchedMovies, movieServiceFeign.findMoviesByIds(watchedMovieIds));
    }
}
