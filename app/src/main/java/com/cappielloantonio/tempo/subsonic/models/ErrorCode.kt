package com.cappielloantonio.tempo.subsonic.models

class ErrorCode(val value: Int) {

    companion object {
        var GENERIC_ERROR = 0
        var REQUIRED_PARAMETER_MISSING = 10
        var INCOMPATIBLE_VERSION_CLIENT = 20
        var INCOMPATIBLE_VERSION_SERVER = 30
        var WRONG_USERNAME_OR_PASSWORD = 40
        var TOKEN_AUTHENTICATION_NOT_SUPPORTED = 41
        var USER_NOT_AUTHORIZED = 50
        var TRIAL_PERIOD_OVER = 60
        var DATA_NOT_FOUND = 70
    }
}