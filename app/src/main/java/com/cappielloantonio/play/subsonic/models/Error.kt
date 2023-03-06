package com.cappielloantonio.play.subsonic.models;

import com.cappielloantonio.play.subsonic.utils.converter.ErrorCodeConverter;
import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class Error {
    @Attribute(converter = ErrorCodeConverter.class)
    protected ErrorCode code;
    @Attribute
    protected String message;

    public ErrorCode getCode() {
        return code;
    }

    public void setCode(ErrorCode value) {
        this.code = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }
}
