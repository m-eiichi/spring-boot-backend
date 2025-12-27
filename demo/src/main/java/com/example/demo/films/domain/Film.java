package com.example.demo.films.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import com.example.demo.films.domain.vo.FilmId;
import com.example.demo.films.domain.vo.FilmTitle;
import com.example.demo.films.domain.vo.FilmDescription;
import com.example.demo.films.domain.vo.FilmReleaseYear; 

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)  
public class Film {
    private FilmId filmId;
    private FilmTitle title;
    private FilmDescription description;
    private FilmReleaseYear releaseYear;

    /**
     * 新規フィルム作成（ファクトリーメソッド）
     */
    public static Film create(FilmId filmId, FilmTitle title, FilmDescription description, FilmReleaseYear releaseYear) {
        Film film = new Film();
        film.filmId = filmId;
        film.title = title;
        film.description = description;
        film.releaseYear = releaseYear;
        return film;
    }

    /**
     * 既存フィルムの再構成（リポジトリから取得時に使用）
     */
    public static Film reconstruct(FilmId filmId, FilmTitle title, FilmDescription description, FilmReleaseYear releaseYear) {
        Film film = new Film();
        film.filmId = filmId;
        film.title = title;
        film.description = description;
        film.releaseYear = releaseYear;
        return film;
    }

    /**
     * タイトル変更
     */
    public void changeName(FilmTitle newTitle) {

        this.title = newTitle;
    }

}