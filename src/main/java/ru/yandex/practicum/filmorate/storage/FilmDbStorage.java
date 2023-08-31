package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
        return jdbcTemplate.queryForObject("select films_id as id, f.name, description as description, releasedate as releaseDate, duration , r.reating_id as rating , ul.id_user  as usersLikeMovie, fg.ganre_id as genre, g.name_ganre as nameGanre, r.name as nameMPA\n" +
                "from films f\n" +
                "              LEFT JOIN reating r on r.reating_id = f.rating\n" +
                "                LEFT JOIN users_like ul on f.films_id = ul.id_films\n" +
                "                LEFT JOIN film_ganre fg  on fg.film_id = f.films_id \n" +
                "                left join genre g on g.ganer_id = fg.ganre_id where films_id =?;", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                MPA mpa = new MPA();
                Set<Integer> usersLikeMovie = new HashSet<>();
                Set<Genres> genres = new HashSet<>();


                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                mpa.setId(rs.getInt("rating"));
                mpa.setName(rs.getString("nameMPA"));
                film.setMpa(mpa);
                do {
                    Genres genres1 = new Genres();
                    genres1.setId(rs.getInt("genre"));
                    genres1.setName(rs.getString("nameGanre"));
                    usersLikeMovie.add(rs.getInt("usersLikeMovie"));
                    genres.add(genres1);
                } while (rs.next());
                film.setGenres(genres);
                film.setUsersLikeMovie(usersLikeMovie);
                int like=0;

                for (Integer integer : usersLikeMovie) {
                    if (integer > 0 ){
                        like++;
                    }
                }

                film.setRate(like);





                return film;
            }
        }, idFilm );
    }



    @Override
    public Film postFilm(Film film) {
        System.out.println(film);
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
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Integer keyFilm = keyHolder.getKey().intValue();

        if (film.getGenres() == null){
            return getFilm(keyFilm);
        }

        for (Genres genres : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO film_ganre (film_id, ganre_id) VALUES(?,?);", keyFilm,
                    genres.getId());
        }
        return getFilm(keyFilm);
    }

    @Override
    public Film putFilm(Film film) {
       /* Date date = Date.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("update films set name = ?, description =?, releasedate = ?, duration  = ?, rating =?\n" +
                    "where films_id = ?;");
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, sqlDate);
            ps.setInt(4, film.getDuration());
            ps.setDouble(5, film.getRating());
            ps.setLong(6, film.getId()); // Assuming getId() returns the films_id

            return ps;
        }, keyHolder);

*/


        jdbcTemplate.update("update films set name = ?, description =?, releasedate = ?, duration  = ?, rating =?" +
                " where films_id = ?;", film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());

        if (film.getGenres() == null){
            return getFilm(film.getId());
        }


        for (Genres genres : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO film_ganre (film_id, ganre_id) VALUES(?,?);", film.getId(),
                    genres);
        }


        return getFilm(film.getId());
    }


    public void addLikeFilm(int id, int userId) {
       jdbcTemplate.update("INSERT INTO users_like (id_user, id_films)\n" +
               "VALUES(?,?);", userId ,id );
    }

    public void deleteLikeFilm(int id, int userId) {
        jdbcTemplate.update("delete from users_like where id_user =? and id_films = ?;", userId ,id );
    }
}
