package com.example.demo.films.presentation.controller;

import com.example.demo.films.presentation.mapper.FilmPresentationMapper;
import com.example.demo.films.presentation.response.FilmResponse;
import com.example.demo.films.application.usecase.*;
import com.example.demo.films.application.dto.FilmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/films")
@RequiredArgsConstructor
public class FilmsController {
    private final CreateFilmUseCase createFilmUseCase;
    private final GetFilmUseCase getFilmUseCase;
    private final ListFilmsUseCase listFilmsUseCase;
    private final UpdateFilmUseCase updateFilmUseCase;
    private final DeleteFilmUseCase deleteFilmUseCase; 
    private final FilmPresentationMapper presentationMapper; 


    @GetMapping
    public ResponseEntity<List<FilmResponse>> listemployees(
        @RequestParam(required = false) Boolean activeOnly
    ) {
         List<FilmDTO> employees;
       
        employees = listFilmsUseCase.execute();

        List<FilmResponse> response = presentationMapper.toResponseList(employees);

        return ResponseEntity.ok(response);
    }


}
