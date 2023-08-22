package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film implements Comparable<Film> {
    @EqualsAndHashCode.Exclude
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private Set<String> genre;
    private String rating;
    private int duration;
    private Set<Integer> usersLikeMovie = new HashSet<>();
    private int likes = 0;

    public void setUsersLikeMovie(Set<Integer> usersLikeMovie) {
        this.setLikes(usersLikeMovie.size());
        this.usersLikeMovie = usersLikeMovie;
    }

    public void addLike(int id) {
        usersLikeMovie.add(id);
        setLikes(getLikes() + 1);
    }

    public void deleteLike(int id) {
        usersLikeMovie.remove(id);
        setLikes(getLikes() - 1);
    }

    @Override
    public int compareTo(Film o) {
        return Integer.compare(o.getLikes(), this.likes);
    }

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
