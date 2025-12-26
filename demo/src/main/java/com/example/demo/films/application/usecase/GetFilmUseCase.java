package com.example.demo.films.application.usecase;

import com.example.demo.films.application.dto.FilmDTO;
import com.example.demo.films.application.mapper.FilmApplicationMapper;
import com.example.demo.films.domain.Film;
import com.example.demo.films.domain.FilmRepository;
import com.example.demo.films.domain.vo.FilmId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * フィルム取得ユースケース
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetFilmUseCase {

    private final FilmRepository filmRepository;
    private final FilmApplicationMapper mapper;

    /**
     * IDでフィルムを取得
     */
    public FilmDTO execute(Long id) {
        Film film = filmRepository.findById(new FilmId(id))
                .orElseThrow(() -> new RuntimeException("Film not found with id: " + id));
        return mapper.toDTO(film);
    }
}
