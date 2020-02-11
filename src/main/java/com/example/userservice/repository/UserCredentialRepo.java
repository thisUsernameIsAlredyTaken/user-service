package com.example.userservice.repository;

import com.example.userservice.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface UserCredentialRepo extends JpaRepository<UserCredential, String> {

    @Query(nativeQuery = true,
            value = "select register_user(:username, :password_hash, :first_name, :last_name)")
    boolean registerUser(@Param("username") String username,
                      @Param("password_hash") String passwordHash,
                      @Param("first_name") String firstName,
                      @Param("last_name") String lastName);
}
