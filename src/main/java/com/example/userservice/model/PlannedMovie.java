package com.example.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@IdClass(CompositeId.class)
public class PlannedMovie {

    @Id
    private String username;

    @Id
    private String movieId;

    private Date date;
}
