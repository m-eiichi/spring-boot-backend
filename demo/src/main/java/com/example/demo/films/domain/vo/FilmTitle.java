package com.example.demo.films.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class FilmTitle {
    private final String value;

    public FilmTitle(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("FilmTitle must not be null or empty");
        }
        this.value = value;
    }
}