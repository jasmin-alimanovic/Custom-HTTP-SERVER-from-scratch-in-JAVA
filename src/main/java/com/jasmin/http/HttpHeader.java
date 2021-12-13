package com.jasmin.http;

public enum HttpHeader {
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LANGUAGE("Content-Language"),
    CONTENT_LOCATION("Content-Location"),
    ALLOW("Allow"),
    CONNECTION("Connection"),
    USER_AGENT("User-Agent"),
    HOST("Host"),
    UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
    CACHE_CONTROL("Cache-Control"),
    SEC_FETCH_DEST("Sec-Fetch-Dest"),
    SEC_FETCH_MODE("Sec-Fetch-Mode"),
    SEC_FETCH_SITE("Sec-Fetch-Site"),
    SEC_FETCH_USER("Sec-Fetch-User");


    public final String HEADER_NAME;

    HttpHeader(String HEADER_NAME) {
        this.HEADER_NAME = HEADER_NAME;
    }
}
