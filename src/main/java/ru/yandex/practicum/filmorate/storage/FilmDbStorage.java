package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage{
    public final JdbcTemplate jdbcTemplate;
    @Override
    public List<Film> getFilms() {
        List<Film> listIdFilm = jdbcTemplate.query("select films_id as id from films f ;", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                return film;
            }
        });
        ArrayList<Film> filmArrayList = new ArrayList<>();
        for (Film film : listIdFilm) {
            filmArrayList.add(getFilm(film.getId()));
        }


        return filmArrayList;

    }

    public Film getFilm(Integer idFilm){
        return jdbcTemplate.queryForObject(" select films_id as id, f.name, description as description, releasedate as releaseDate, duration , r.reating_id as rating , ul.id_user as usersLikeMovie, fg.ganre_id as genre\n" +
                " from films f \n" +
                " LEFT JOIN reating r on r.reating_id = f.rating\n" +
                " LEFT JOIN users_like ul on f.films_id = ul.id_films\n" +
                " LEFT JOIN film_ganre fg  on fg.film_id = f.films_id \n" +
                " where films_id =?;", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Set<Integer> usersLikeMovie = new HashSet<>();
                Set<Integer> genres = new HashSet<>();
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setRating(rs.getInt("rating"));
                film.setDuration(rs.getInt("duration"));
                do {
                    genres.add(rs.getInt("genre"));
                    usersLikeMovie.add(rs.getInt("usersLikeMovie"));
                } while (rs.next());
                film.setGenre(genres);
                film.setLikes(usersLikeMovie.size());
                film.setUsersLikeMovie(usersLikeMovie);
                return film;
            }
        }, idFilm );
    }



    @Override
    public Film postFilm(Film film) {
       /* jdbcTemplate.update("INSERT INTO films (name, description, releasedate, duration, rating)\n" +
                "VALUES" + " (?, ?, ?, ?, ?);" , film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRating());*/

        Date date = Date.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());


        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO films (name, description, releasedate, duration, rating) " +
                    "VALUES (?, ?, ?, ?, ?)", new String[]{"films_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3,  sqlDate);
            ps.setInt(4, film.getDuration());
            ps.setDouble(5, film.getRating());

            return ps;
        }, keyHolder);

        Integer integer = keyHolder.getKey().intValue();
        return getFilm(integer);
    }

    @Override
    public Film putFilm(Film film) {
        return null;
    }
}
