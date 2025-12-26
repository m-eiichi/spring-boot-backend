package com.example.demo.films.domain;

import com.example.demo.films.domain.vo.FilmId;
import com.example.demo.films.domain.vo.FilmTitle;


import java.util.List;
import java.util.Optional;

/**
 * フィルムリポジトリインターフェース（ドメイン層）
 * インフラストラクチャ層で実装される
 */
public interface FilmRepository {

    /**
     * フィルムを保存
     */
    Film save(Film film);

    /**
     * フィルムを削除
     */
    void delete(Film film);

    /**
     * IDでフィルムを検索
     */
    Optional<Film> findById(FilmId id);

    /**
     * タイトルでフィルムを検索
     */
    Optional<Film> findByTitle(FilmTitle title);

    /**
     * 全フィルムを取得
     */
    List<Film> findAll();


    /**
     * 次のIDを生成（オプション）
     */
    FilmId nextIdentity();
}