package com.example.demo.films.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmResponse {
    private Long filmId;
    private String title;
    private String description;
    private Integer releaseYear;
}
