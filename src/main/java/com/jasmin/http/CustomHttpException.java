package com.jasmin.http;

public class CustomHttpException extends Exception {

    private final HttpStatusCode errorCode;
    public CustomHttpException(HttpStatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode(){
        return this.errorCode;
    }
}
