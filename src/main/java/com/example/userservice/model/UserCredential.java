package com.example.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UserCredential {

    @Id
    private String username;

    private String passwordHash;

    private String authorities;

    public String[] getAuthorities() {
        return authorities.split(",");
    }

    public void setAuthorities(String... authorities) {
        this.authorities = String.join(",", authorities);
    }
}
