package com.cappielloantonio.play.subsonic.utils.converter;

import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.tickaroo.tikxml.TypeConverter;

public class ResponseStatusConverter implements TypeConverter<ResponseStatus> {
    @Override
    public ResponseStatus read(String value) throws Exception {
        return new ResponseStatus(value);
    }

    @Override
    public String write(ResponseStatus value) throws Exception {
        return value.getValue();
    }
}