package com.jasmin.http;

public class BadHttpVersionException extends Exception {

    private final HttpStatusCode errorCode;
    public BadHttpVersionException(HttpStatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode(){
        return this.errorCode;
    }
}
