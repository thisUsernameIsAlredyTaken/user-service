package com.example.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@IdClass(CompositeId.class)
public class WatchedMovie {

    @Id
    private String username;

    @Id
    private String movieId;

    private Integer rating;

    private Date date;

    private String message;
}
