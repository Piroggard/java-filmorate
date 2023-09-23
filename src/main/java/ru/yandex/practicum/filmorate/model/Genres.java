package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Genres implements Comparable<Genres> {
    private Integer id;
    private String name;

    public Genres(Integer id) {
        this.id = id;
    }

    @Override
    public int compareTo(Genres other) {
        return this.id.compareTo(other.id);
    }
}
