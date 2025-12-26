package com.example.demo.films.application.usecase;

import com.example.demo.films.application.dto.FilmDTO;
import com.example.demo.films.application.mapper.FilmApplicationMapper;
import com.example.demo.films.domain.Film;
import com.example.demo.films.domain.FilmRepository;
import com.example.demo.films.domain.vo.FilmDirector;
import com.example.demo.films.domain.vo.FilmReleaseYear;
import com.example.demo.films.domain.vo.FilmTitle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

/**
 * フィルム作成ユースケース
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CreateFilmUseCase {

    private final FilmRepository filmRepository;
    private final FilmApplicationMapper mapper;

    /**
     * フィルムを作成
     */
    public FilmDTO execute(String title, String director, Date releaseYear) {
        Film film = Film.create(
                filmRepository.nextIdentity(),
                new FilmTitle(title),
                new FilmDirector(director),
                new FilmReleaseYear(releaseYear)
        );

        Film savedFilm = filmRepository.save(film);
        return mapper.toDTO(savedFilm);
    }
}
