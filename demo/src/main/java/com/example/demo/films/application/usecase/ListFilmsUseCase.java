package com.example.demo.films.application.usecase;

import com.example.demo.films.application.dto.FilmDTO;
import com.example.demo.films.application.mapper.FilmApplicationMapper;
import com.example.demo.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 従業員一覧取得ユースケース
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ListFilmsUseCase {

    private final FilmRepository filmRepository;
    private final FilmApplicationMapper mapper;

    /**
     * 全従業員を取得
     */
    public List<FilmDTO> execute() {
        return mapper.toDTOList(filmRepository.findAll());
    }

}