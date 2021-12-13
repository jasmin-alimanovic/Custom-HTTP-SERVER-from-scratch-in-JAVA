package com.jasmin.http;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    public static HttpVersion CheckIfVersionIsValid(String literal) throws BadHttpVersionException {
        Matcher matcher = httpVersionRegexPattern.matcher(literal);

        if(!matcher.find() || matcher.groupCount() != 2){
            throw new BadHttpVersionException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
        for (HttpVersion version : HttpVersion.values())
        {
            if (version.LITERAL.equals(literal))
                return version;
        }
        throw new BadHttpVersionException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
    }

}
