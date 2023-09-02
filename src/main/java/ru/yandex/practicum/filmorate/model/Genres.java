package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Genres implements Comparable<Genres> {
    Integer id;
    String name;

    public Genres(Integer id) {
        this.id = id;
    }

    @Override
    public int compareTo(Genres other) {
        return this.id.compareTo(other.id);
    }
}
