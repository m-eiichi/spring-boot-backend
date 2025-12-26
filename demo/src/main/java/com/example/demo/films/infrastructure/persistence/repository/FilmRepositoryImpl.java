package com.example.demo.films.infrastructure.persistence.repository;

import com.example.demo.films.domain.Film;
import com.example.demo.films.domain.FilmRepository;
import com.example.demo.films.domain.vo.FilmId;
import com.example.demo.films.domain.vo.FilmTitle;
import com.example.demo.films.infrastructure.persistence.entity.FilmEntity;
import com.example.demo.films.infrastructure.persistence.mapper.FilmPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final FilmJpaRepository jpaRepository;
    private final FilmPersistenceMapper mapper;

    @Override
    public Film save(Film film) {
        FilmEntity entity = mapper.toEntity(film);
        FilmEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Film film) {
        FilmEntity entity = mapper.toEntity(film);
        jpaRepository.delete(entity);
    }

    @Override
    public Optional<Film> findById(FilmId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Film> findByTitle(FilmTitle title) {
        return jpaRepository.findByTitle(title.getValue())
                .map(mapper::toDomain);
    }


    @Override
    public List<Film> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public FilmId nextIdentity() {
        return new FilmId(System.currentTimeMillis());
    }
}
