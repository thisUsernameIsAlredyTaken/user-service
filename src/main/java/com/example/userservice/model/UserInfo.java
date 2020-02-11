package com.example.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class UserInfo {

    @Id
    private String username;

    private String firstName;

    private String lastName;

    private Date registerDate;
}
