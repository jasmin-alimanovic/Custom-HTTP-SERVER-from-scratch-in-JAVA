package com.jasmin.http;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser parser;
    @BeforeAll
    public void beforeClass(){
        parser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request;
        try {
            request = parser.parseHttpRequest(generateValidGetRequest());
            assertNotNull(request);
            assertEquals(request.getMethod(), HttpMethod.HEAD);
            assertEquals(request.getVersion(), HttpVersion.HTTP_1_1);
            assertEquals(request.getRequestTarget(), "/");
        } catch (CustomHttpException | BadHttpVersionException e) {
            fail();
        }
    }

    @Test
    void parseHttpRequestBadMethodName1() {
        HttpRequest request = null;
        try {
            request = parser.parseHttpRequest(generateBadMethodRequest1());
            fail();
        } catch (CustomHttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        } catch (BadHttpVersionException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    @Test()
    void parseHttpRequestBadMethodName2() {
        HttpRequest request = null;
        try {
            request = parser.parseHttpRequest(generateBadMethodRequest2());
            fail();
        } catch (CustomHttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        } catch (BadHttpVersionException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }
    @Test()
    void parseHttpRequestNoRequestTarget() {
        HttpRequest request;
        try {
            request = parser.parseHttpRequest(generateNoRequestTarget());
            assertNull(request.getRequestTarget());
        } catch (CustomHttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        } catch (BadHttpVersionException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }
    @Test()
    void parseHttpRequestBadRequestLine() {
        HttpRequest request = null;
        try {
            request = parser.parseHttpRequest(generateBadRequestLine());
        } catch (BadHttpVersionException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        } catch (CustomHttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test()
    void parseHttpRequestBadRequestNoCR() {
        HttpRequest request = null;
        try {
            request = parser.parseHttpRequest(generateBadRequestNoCR());
        }  catch (BadHttpVersionException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        } catch (CustomHttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    @Test()
    void parseHttpRequestBadRequestNoLF() {
        HttpRequest request = null;
        try {
            request = parser.parseHttpRequest(generateBadRequestNoCR());
        }  catch (BadHttpVersionException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        } catch (CustomHttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    @Test()
    void parseHttpRequestVersionNotSupported() {
        HttpRequest request = null;
        try {
            request = parser.parseHttpRequest(generateBadRequestVersionNotSupported());
        }  catch (BadHttpVersionException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        } catch (CustomHttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    
    private InputStream generateValidGetRequest()
    {
        String rawHtml = "HEAD / HTTP/1.1\r\n" +
                "Host: 127.0.0.1:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawHtml.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }
    private InputStream generateBadMethodRequest1()
    {
        String rawHtml = "gEt / HTTP/1.1\r\n" +
                "Host: 127.0.0.1:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawHtml.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }
    private InputStream generateBadMethodRequest2()
    {
        String rawHtml = "GETSS / HTTP/1.1\r\n" +
                "Host: 127.0.0.1:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawHtml.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateNoRequestTarget()
    {
        String rawHtml = "GET HTTP/1.1\r\n" +
                "Host: 127.0.0.1:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawHtml.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }
    private InputStream generateBadRequestLine()
    {
        String rawHtml = "GET / EGt HTTP/1.1\r\n" +
                "Host: 127.0.0.1:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawHtml.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateBadRequestNoCR()
    {
        String rawHtml = "GET / HTTP/1.1\n" +
                "Host: 127.0.0.1:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawHtml.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }
    private InputStream generateBadRequestNoLF()
    {
        String rawHtml = "GET / HTTP/1.1\r" +
                "Host: 127.0.0.1:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawHtml.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }
    private InputStream generateBadRequestVersionNotSupported()
    {
        String rawHtml = "GET / HTTP/2.1\r\n" +
                "Host: 127.0.0.1:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawHtml.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }
}