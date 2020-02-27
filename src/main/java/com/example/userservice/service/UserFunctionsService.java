package com.example.userservice.service;

import com.example.userservice.feign.MovieServiceFeign;
import com.example.userservice.model.PlannedMovie;
import com.example.userservice.model.WatchedMovie;
import com.example.userservice.repository.PlannedMovieRepo;
import com.example.userservice.repository.UserCredentialRepo;
import com.example.userservice.repository.WatchedMovieRepo;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserFunctionsService {

    @Autowired
    private WatchedMovieRepo watchedMovieRepo;

    @Autowired
    private PlannedMovieRepo plannedMovieRepo;

    @Autowired
    private UserCredentialRepo userCredentialRepo;

    @Autowired
    private MovieServiceFeign movieServiceFeign;

    public Map<String, Object> getWatchedById(String username, String movieId) {
        WatchedMovie watchedMovie = watchedMovieRepo.findByUsernameAndMovieId(username, movieId).orElse(null);
        if (watchedMovie == null) {
            return null;
        }
        return mergeInfoWatched(Arrays.asList(watchedMovie),
                movieServiceFeign.findMoviesByIds(Arrays.asList(watchedMovie.getMovieId()))).get(0);
    }

    public boolean patchWatched(String username, String movieId, Integer score, String message) {
        Optional<WatchedMovie> watchedOptional = watchedMovieRepo.findByUsernameAndMovieId(username, movieId);
        if (!watchedOptional.isPresent()) {
            return false;
        }
        WatchedMovie watched = watchedOptional.get();
        if (score != null) {
            if (score < 1 || score > 10) {
                watched.setRating(null);
            } else {
                watched.setRating(score);
            }
        }
        if (message != null) {
            watched.setMessage(message);
        }
        watchedMovieRepo.save(watched);
        return true;
    }

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
        plannedMovieRepo.deleteByUsernameAndMovieId(username, movieId);
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
        watchedMovieRepo.deleteByUsernameAndMovieId(username, movieId);
        plannedMovieRepo.save(pm);
        return true;
    }

    private List<Map<String, Object>> mergeInfoPlanned(List<PlannedMovie> plannedMovies,
                                                       List<Map<String, Object>> movies) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (PlannedMovie plannedMovie : plannedMovies) {
            Map<String, Object> newMovie = new HashMap<>(
                    movies.stream().filter(movie ->
                            movie.get("id").equals(plannedMovie.getMovieId())).findAny().get()
            );
            newMovie.put("add_date", plannedMovie.getDate());
            newMovie.put("listed", "planned");
            res.add(newMovie);
        }
        return res;
    }

    private List<Map<String, Object>> mergeInfoWatched(List<WatchedMovie> watchedMovies,
                                                       List<Map<String, Object>> movies) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (WatchedMovie watchedMovie : watchedMovies) {
            Map<String, Object> newMovie = new HashMap<>(
                    movies.stream().filter(movie ->
                            movie.get("id").equals(watchedMovie.getMovieId())).findAny().get()
            );
            newMovie.put("user_score", watchedMovie.getRating());
            newMovie.put("user_message", watchedMovie.getMessage());
            newMovie.put("add_date", watchedMovie.getDate());
            newMovie.put("listed", "watched");
            res.add(newMovie);
        }
        return res;
    }

    public List<Map<String, Object>> findAllPlannedByUsername(String username, int page, int pageSize) {
        int maxPage = plannedMovieRepo.countByUsername(username) / pageSize;
        page = Math.min(maxPage, page);
        if (page == -1) page = 0;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("date").ascending());
        List<PlannedMovie> plannedMovies = plannedMovieRepo.findAllByUsername(username, pageable);
        List<String> plannedMoviesIds = new ArrayList<>();
        for (PlannedMovie plannedMovie : plannedMovies) {
            plannedMoviesIds.add(plannedMovie.getMovieId());
        }
        return mergeInfoPlanned(plannedMovies, movieServiceFeign.findMoviesByIds(plannedMoviesIds));
    }

    public List<Map<String, Object>> findAllWatchedByUsername(String username, int page, int pageSize) {
        int maxPage = (watchedMovieRepo.countByUsername(username) - 1) / pageSize;
        page = Math.min(maxPage, page);
        if (page == -1) page = 0;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("date").descending());
        List<WatchedMovie> watchedMovies = watchedMovieRepo.findAllByUsername(username, pageable);
        List<String> watchedMovieIds = new ArrayList<>();
        for (WatchedMovie watchedMovie : watchedMovies) {
            watchedMovieIds.add(watchedMovie.getMovieId());
        }
        return mergeInfoWatched(watchedMovies, movieServiceFeign.findMoviesByIds(watchedMovieIds));
    }

    public int watchedCount(String username) {
        return watchedMovieRepo.countByUsername(username);
    }

    public int plannedCount(String username) {
        return plannedMovieRepo.countByUsername(username);
    }

    public void deleteListed(String username, String movieId) {
        plannedMovieRepo.deleteByUsernameAndMovieId(username, movieId);
        watchedMovieRepo.deleteByUsernameAndMovieId(username, movieId);
    }

    public String listedStatus(String username, String movieId) {
        return userCredentialRepo.getListedStatus(movieId, username).orElse(null);
    }

    public List<Map<String, Object>> recommend(String username, int count) {
        List<Map<String, Object>> idsAndScores = watchedMovieRepo.findWatchedIdsByUsername(username);
        List<String> ids = new ArrayList<>();
        List<String> scores = new ArrayList<>();
        for (Map<String, Object> idAndScore : idsAndScores) {
            ids.add((String) idAndScore.get("movie_id"));
            Integer rating = (Integer) idAndScore.get("rating");
            rating = Optional.ofNullable(rating).orElse(5);
            scores.add(rating.toString());
        }
        return movieServiceFeign.recommend(String.join(",", ids),
                String.join(",", scores),
                0, count);
    }
}
