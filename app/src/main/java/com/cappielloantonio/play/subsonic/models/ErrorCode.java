package com.cappielloantonio.play.subsonic.models;

public class ErrorCode {
    public static int GENERIC_ERROR = 0;
    public static int REQUIRED_PARAMETER_MISSING = 10;
    public static int INCOMPATIBLE_VERSION_CLIENT = 20;
    public static int INCOMPATIBLE_VERSION_SERVER = 30;
    public static int WRONG_USERNAME_OR_PASSWORD = 40;
    public static int TOKEN_AUTHENTICATION_NOT_SUPPORTED = 41;
    public static int USER_NOT_AUTHORIZED = 50;
    public static int TRIAL_PERIOD_OVER = 60;
    public static int DATA_NOT_FOUND = 70;

    private final int value;

    public ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
