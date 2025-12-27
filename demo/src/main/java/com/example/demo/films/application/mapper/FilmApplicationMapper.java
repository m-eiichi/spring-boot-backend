package com.example.demo.films.application.mapper;

import com.example.demo.films.application.dto.FilmDTO;
import com.example.demo.films.domain.Film;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * ドメインモデル ↔ DTO の変換（アプリケーション層、MapStruct使用）
 */
@Mapper(componentModel = "spring")
public interface FilmApplicationMapper {

    /**
     * ドメインモデル → DTO
     */
    @Mapping(source = "filmId.value", target = "filmId")
    @Mapping(source = "title.value", target = "title")
    @Mapping(source = "description.value", target = "description")
    @Mapping(source = "releaseYear.value", target = "releaseYear")
    FilmDTO toDTO(Film film);

    /**
     * ドメインモデルリスト → DTOリスト
     */
    List<FilmDTO> toDTOList(List<Film> films);
}