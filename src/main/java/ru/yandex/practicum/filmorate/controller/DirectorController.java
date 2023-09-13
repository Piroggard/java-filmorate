package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@AllArgsConstructor
public class DirectorController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final FilmService filmService;

    @GetMapping("/directors")
    public List<Director> getDirectors() {
        log.info("");
        return filmService.getDirectors();
    }

    @GetMapping("/directors/{id}")
    public Director getDirectorsById(@PathVariable Integer id) {
        log.info("Получение данных по {}" , id);
        return filmService.getDirectorsById(id);
    }

    @PostMapping("/directors")
    public Director postDirectors(@RequestBody Director director){
        log.info(" "+ director);
        return filmService.postDirectors(director);
    }

    @PutMapping("/directors")
    public Director putDirectors(@RequestBody Director director){
        return filmService.putDirectors(director);
    }

}