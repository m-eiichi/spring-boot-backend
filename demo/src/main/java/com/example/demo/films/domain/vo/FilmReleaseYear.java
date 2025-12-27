package com.example.demo.films.domain.vo;

import lombok.Getter;


import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class FilmReleaseYear {
    private final Integer value;

    public FilmReleaseYear(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("FilmReleaseYear must not be null");
        }
        this.value = value;
    }
    
}
