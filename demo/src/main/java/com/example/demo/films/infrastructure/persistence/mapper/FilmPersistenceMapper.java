package com.example.demo.films.infrastructure.persistence.mapper;

import com.example.demo.films.domain.Film;
import com.example.demo.films.domain.vo.FilmDirector;
import com.example.demo.films.domain.vo.FilmId;
import com.example.demo.films.domain.vo.FilmReleaseYear;
import com.example.demo.films.domain.vo.FilmTitle;
import com.example.demo.films.infrastructure.persistence.entity.FilmEntity;
import org.springframework.stereotype.Component;

@Component
public class FilmPersistenceMapper {

    /**
     * ドメインモデルからJPAエンティティへ変換
     */
    public FilmEntity toEntity(Film film) {
        if (film == null) {
            return null;
        }

        FilmEntity entity = new FilmEntity();
        if (film.getFilmId() != null) {
            entity.setFilmId(film.getFilmId().getValue());
        }
        entity.setTitle(film.getTitle().getValue());
        entity.setDirector(film.getDirector().getValue());
        entity.setReleaseYear(film.getReleaseYear().getValue());
        // entity.setActive(true);

        return entity;
    }

    /**
     * JPAエンティティからドメインモデルへ変換
     */
    public Film toDomain(FilmEntity entity) {
        if (entity == null) {
            return null;
        }

        return Film.reconstruct(
            new FilmId(entity.getFilmId()),
            new FilmTitle(entity.getTitle()),
            new FilmDirector(entity.getDirector()),
            new FilmReleaseYear(entity.getReleaseYear())
        );
    }
}
