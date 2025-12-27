package com.example.demo.films.domain.vo;

import lombok.Getter;
import lombok.EqualsAndHashCode;

@Getter
@EqualsAndHashCode
public class FilmDescription {
    private final String value;

    public FilmDescription(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("FilmDescription must not be null or empty");
        }
        this.value = value;
    }
}