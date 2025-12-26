package com.example.demo.films.domain.vo;

import lombok.Getter;

import java.sql.Date;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class FilmReleaseYear {
    private final Date value;

    public FilmReleaseYear(Date value) {
        if (value == null) {
            throw new IllegalArgumentException("FilmReleaseYear must not be null");
        }
        this.value = value;
    }
    
}
