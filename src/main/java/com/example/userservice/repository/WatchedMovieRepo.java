package com.example.userservice.repository;

import com.example.userservice.model.CompositeId;
import com.example.userservice.model.WatchedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchedMovieRepo extends JpaRepository<WatchedMovie, CompositeId> {

    List<WatchedMovie> findAllByUsername(String username);
}
