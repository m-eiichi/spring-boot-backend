package com.example.demo.films.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class FilmId {
    private final Long value;

    public FilmId(Long value) {

        if (value == null || value.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("EmployeeCode must not be null or empty");
        }
        this.value = value;
    }
    
}