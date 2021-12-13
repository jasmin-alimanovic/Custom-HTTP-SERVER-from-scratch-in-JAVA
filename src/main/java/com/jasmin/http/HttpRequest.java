package com.jasmin.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private HttpVersion version;
    private String requestTarget;
    private final HashMap<HttpHeader, String> headers;

    public HttpRequest() {
        headers = new HashMap<>();
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(String methodName) throws CustomHttpException {
        for (HttpMethod method : HttpMethod.values())
        {
            if (methodName.equals(method.name())){
                this.method = method;
                return;
            }
        }
        throw new CustomHttpException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(String versionLiteral) throws BadHttpVersionException, CustomHttpException {
        HttpVersion version = HttpVersion.CheckIfVersionIsValid(versionLiteral);
        if (version == null)
        {
            throw new CustomHttpException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
        this.version = version;

    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public void setRequestTarget(String requestTarget) throws CustomHttpException {
        if (requestTarget == null || requestTarget.length() == 0)
            throw new CustomHttpException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        this.requestTarget = requestTarget;
    }

    public HashMap<HttpHeader, String> getHeaders() { return this.headers; }

    public void setHeader(String headerKey, String headerValue)  {
        for(HttpHeader h: HttpHeader.values())
        {
            if(headerKey.equals(h.name())){
                this.headers.put(h, headerValue);
                return;
            }
        }
        //throw new CustomHttpException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }
}
