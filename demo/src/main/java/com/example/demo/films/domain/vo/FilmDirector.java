package com.example.demo.films.domain.vo;

import lombok.Getter;
import lombok.EqualsAndHashCode;

@Getter
@EqualsAndHashCode
public class FilmDirector {
    private final String value;

    public FilmDirector(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("FilmDirector must not be null or empty");
        }
        this.value = value;
    }
}