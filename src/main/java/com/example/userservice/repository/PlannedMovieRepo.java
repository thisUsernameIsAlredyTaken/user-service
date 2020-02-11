package com.example.userservice.repository;

import com.example.userservice.model.CompositeId;
import com.example.userservice.model.PlannedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannedMovieRepo extends JpaRepository<PlannedMovie, CompositeId> {

    List<PlannedMovie> findAllByUsername(String username);
}
