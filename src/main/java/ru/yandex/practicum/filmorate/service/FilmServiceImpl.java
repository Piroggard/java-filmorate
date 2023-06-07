package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService{
    public final FilmStorage filmStorage;

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public Film postFilm(Film film) {
        return filmStorage.postFilm(film);
    }

    @Override
    public Film putFilm(Film film) {
        return filmStorage.putFilm(film);
    }
}
