package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MPA {
    public MPA(Integer id) {
        this.id = id;
    }

    Integer id;
    String name;
}
