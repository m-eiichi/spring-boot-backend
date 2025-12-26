package com.example.demo.films.application.usecase;

import com.example.demo.films.application.dto.FilmDTO;
import com.example.demo.films.application.mapper.FilmApplicationMapper;
import com.example.demo.films.domain.Film;
import com.example.demo.films.domain.FilmRepository;
import com.example.demo.films.domain.vo.FilmId;
import com.example.demo.films.domain.vo.FilmTitle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * フィルム更新ユースケース
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UpdateFilmUseCase {

    private final FilmRepository filmRepository;
    private final FilmApplicationMapper mapper;

    /**
     * フィルムのタイトルを更新
     */
    public FilmDTO execute(Long id, String newTitle) {
        Film film = filmRepository.findById(new FilmId(id))
                .orElseThrow(() -> new RuntimeException("Film not found with id: " + id));

        film.changeName(new FilmTitle(newTitle));

        Film updatedFilm = filmRepository.save(film);
        return mapper.toDTO(updatedFilm);
    }
}
