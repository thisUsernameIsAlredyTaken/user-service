package com.example.userservice.repository;

import com.example.userservice.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, String> {

    boolean existsByEmail(String email);
}
