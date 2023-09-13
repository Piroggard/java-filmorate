package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.validation.ValidationDirectors;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;
import ru.yandex.practicum.filmorate.validation.ValidationGanres;
import ru.yandex.practicum.filmorate.validation.ValidationMpa;

import java.util.List;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final FilmDbStorage filmDbStorage;
    private final ValidationFilm validationFilm = new ValidationFilm();
    private final ValidationGanres validationGanres = new ValidationGanres();
    private final ValidationDirectors validationDirectors = new ValidationDirectors();
    private final ValidationMpa validationMpa = new ValidationMpa();

    @Override
    public List<Film> getFilms() {
        List<Film> filmList = filmDbStorage.getFilms();
       return filmList;
    }

    @Override
    public Film postFilm(Film film) {
        validationFilm.validation(film);
        return filmDbStorage.postFilm(film);
    }

    @Override
    public Film putFilm(Film film) {
        validationFilm.validation(film);
        return filmDbStorage.putFilm(film);
    }

    @Override
    public void addLikeFilm(int id, int userId) {
        filmDbStorage.addLikeFilm(id, userId);
    }

    @Override
    public void deleteLikeFilm(int id, int userId) {
        validationFilm.validationIdFilm(id);
        validationFilm.validationIdFilm(userId);
        filmDbStorage.deleteLikeFilm(id, userId);
    }

    @Override
    public Film getFilm(Integer id) {
        validationFilm.validationIdFilm(id);
        validationFilm.searchValidation(filmDbStorage.getFilm(id));
        return filmDbStorage.getFilm(id);
    }

    @Override
    public List<Film> getListBestMovies(Integer count) {

        if (count == null) {
            return getListBestTenMovies();
        }
        log.info("count " + count);
        return filmDbStorage.getFilmsPopularQuantity(count);
    }

    @Override
    public List<Film> getListBestTenMovies() {
        return filmDbStorage.getFilmsPopular();

    }

    @Override
    public Mpa getMPA(Integer id) {
        validationMpa.validationId(filmDbStorage.getMpa(id));
        return filmDbStorage.getMpa(id);
    }

    @Override
    public List<Mpa> getMPA() {
        List<Mpa> list = filmDbStorage.getMpa();
        return list;
    }

    @Override
    public Genres getGanres(Integer id) {
        validationGanres.validationId(filmDbStorage.getGanres(id));
        return filmDbStorage.getGanres(id);
    }

    @Override
    public List<Genres> getGanres() {
        return filmDbStorage.getGanres();
    }
    @Override
    public List<Director> getDirectors() {
        return filmDbStorage.getDirectors();
    }
    @Override
    public Director getDirectorsById(Integer id) {
        return filmDbStorage.getDirectorsById(id);
    }
    @Override
    public Director postDirectors(Director director){
        validationDirectors.validation(director);
        return filmDbStorage.postDirectors(director);
    }
    @Override
    public Director putDirectors(Director director){
        validationDirectors.validation(director);
        return filmDbStorage.putDirectors(director);
    }

    @Override
    public List<Film> getFilmDirectorYearOrLike(Integer directorId, List<String> sortBy) {
        return filmDbStorage.getFilmDirectorYearOrLike(directorId, sortBy);
    }

    @Override
    public void deleteDirectors(int id) {
        filmDbStorage.deleteDirectors(id);
    }


}
