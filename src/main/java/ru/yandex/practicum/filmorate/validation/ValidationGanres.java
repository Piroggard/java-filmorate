package ru.yandex.practicum.filmorate.validation;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;

@Component
public class ValidationGanres {
    public void validationId(Genres genres){
        if (genres == null) {
            throw new DataNotFoundException("The specified Ganres was not found");
        }
    }
}

