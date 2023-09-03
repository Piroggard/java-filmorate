package ru.yandex.practicum.filmorate.validation;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;


@Component
public class ValidationMpa {
    public void validationId(Mpa mpa){
        if (mpa == null) {
            throw new DataNotFoundException("The specified MPA was not found");
        }
    }
}
