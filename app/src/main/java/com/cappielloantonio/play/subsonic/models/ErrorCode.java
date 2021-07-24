package com.cappielloantonio.play.subsonic.models;

public enum ErrorCode {
    GENERIC_ERROR(0),
    REQUIRED_PARAMETER_MISSING(10),
    INCOMPATIBLE_VERSION_CLIENT(20),
    INCOMPATIBLE_VERSION_SERVER(30),
    WRONG_USERNAME_OR_PASSWORD(40),
    TOKEN_AUTHENTICATION_NOT_SUPPORTED(41),
    USER_NOT_AUTHORIZED(50),
    TRIAL_PERIOD_OVER(60),
    DATA_NOT_FOUND(70);
    private final int value;

    ErrorCode(int v) {
        value = v;
    }

    public int value() {
        return value;
    }

    public static ErrorCode fromValue(int v) {
        for (ErrorCode c : ErrorCode.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }
}
