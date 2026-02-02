package ru.practicum.shareit.booking;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.BadRequestException;

@Component
public class StringToStateConverter implements Converter<String, State> {

    @Override
    public State convert(String source) {
        try {
            return State.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Неверный state: " + source
                    + ". Возможные значения: all, current, past, future, waiting, rejected");
        }
    }
}
