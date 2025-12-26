package com.example.demo.films.presentation.mapper;

import com.example.demo.films.application.dto.FilmDTO;
import com.example.demo.films.presentation.response.FilmResponse;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * アプリケーション層DTO ↔ プレゼンテーション層DTO の変換（MapStruct使用）
 * プレゼンテーション層に配置することで、アプリケーション層への依存のみを持つ
 */
@Mapper(componentModel = "spring")
public interface FilmPresentationMapper {

    /**
     * FilmDTO → FilmResponse
     * フィールド名が一致しているため、明示的な@Mappingは不要だが、
     * コンストラクタベースのマッピングを確実にするため明示的に指定
     */
    FilmResponse toResponse(FilmDTO dto);

    /**
     * FilmDTOリスト → FilmResponseリスト
     */
    List<FilmResponse> toResponseList(List<FilmDTO> dtos);
}
