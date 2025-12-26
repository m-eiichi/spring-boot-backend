package com.example.demo.films.infrastructure.persistence.repository;

import com.example.demo.films.infrastructure.persistence.entity.FilmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmJpaRepository extends JpaRepository<FilmEntity, Long> {

    Optional<FilmEntity> findByTitle(String title);

}
