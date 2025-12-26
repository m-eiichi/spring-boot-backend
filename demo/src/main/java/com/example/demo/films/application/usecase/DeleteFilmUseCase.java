package com.example.demo.films.application.usecase;

import com.example.demo.films.domain.Film;
import com.example.demo.films.domain.FilmRepository;
import com.example.demo.films.domain.vo.FilmId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * フィルム削除ユースケース
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DeleteFilmUseCase {

    private final FilmRepository filmRepository;

    /**
     * フィルムを削除
     */
    public void execute(Long id) {
        Film film = filmRepository.findById(new FilmId(id))
                .orElseThrow(() -> new RuntimeException("Film not found with id: " + id));

        filmRepository.delete(film);
    }
}
