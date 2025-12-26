package com.example.demo.films.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmDTO {
    private Long filmId;
    private String title;
    private String director;
    private Date releaseYear;
}
