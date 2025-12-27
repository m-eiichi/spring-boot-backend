package com.example.demo.films.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "film")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Long filmId;

    @Column(nullable = false)
    private String title;

    @Column( columnDefinition = "text")
    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;

}
