package com.cappielloantonio.play.subsonic.models;

public class Error {
    protected ErrorCode code;
    protected String message;

    /**
     * Gets the value of the code property.
     *
     * @return possible object is
     * {@link ErrorCode }
     */
    public ErrorCode getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link ErrorCode }
     */
    public void setCode(ErrorCode value) {
        this.code = value;
    }

    /**
     * Gets the value of the message property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessage(String value) {
        this.message = value;
    }
}
