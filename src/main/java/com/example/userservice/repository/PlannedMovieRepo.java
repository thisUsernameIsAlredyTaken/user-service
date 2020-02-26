package com.example.userservice.repository;

import com.example.userservice.model.CompositeId;
import com.example.userservice.model.PlannedMovie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannedMovieRepo extends JpaRepository<PlannedMovie, CompositeId> {

    List<PlannedMovie> findAllByUsername(String username, Pageable pageable);

    int countByUsername(String username);

    @Query("select w.movieId from PlannedMovie w where w.username = ?1")
    List<String> findPlannedIdsByUsername(String username);

    void deleteByUsernameAndMovieId(String username, String movieId);
}
