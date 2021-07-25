package com.cappielloantonio.play.subsonic.utils.converter;

import com.cappielloantonio.play.subsonic.models.ErrorCode;
import com.tickaroo.tikxml.TypeConverter;

public class ErrorCodeConverter implements TypeConverter<ErrorCode> {
    @Override
    public ErrorCode read(String value) throws Exception {
        return new ErrorCode(Integer.valueOf(value));
    }

    @Override
    public String write(ErrorCode value) throws Exception {
        return String.valueOf(value.getValue());
    }
}