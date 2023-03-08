package com.cappielloantonio.play.subsonic.models

class ResponseStatus(val value: String) {

    companion object {
        @JvmField
        var OK = "ok"

        @JvmField
        var FAILED = "failed"
    }
}