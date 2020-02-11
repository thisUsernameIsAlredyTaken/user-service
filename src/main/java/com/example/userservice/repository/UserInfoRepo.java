package com.example.userservice.repository;

import com.example.userservice.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, String> {

    @Query(nativeQuery = true,
            value = "select wm.movie_id from watched_movie wm where wm.username = :username")
    List<String> findWatchedByUsername(@Param("username") String username);
}
