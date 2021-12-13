package com.jasmin.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * http parser class
 */
public class HttpParser {


    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 32;
    private static final int CR = 13;
    private static final int LF = 10;


    public HttpRequest parseHttpRequest(InputStream inputStream) throws CustomHttpException, BadHttpVersionException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();
        try {
            parseRequestLine(reader, request);
        }catch (IOException  e){
            e.printStackTrace();
        }

        try {
            parseHeaders(reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, CustomHttpException {
        StringBuilder buffer = new StringBuilder();

        boolean methodParsed = false;
        boolean requestedTargetParsed = false;

        int _byte;
        while((_byte = reader.read()) >=0)
        {
            if (_byte == CR)
            {
                _byte = reader.read();
                if (_byte == LF){
                    LOGGER.debug("Processed request target {}", buffer);
                    if(!methodParsed || !requestedTargetParsed){
                        throw new CustomHttpException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    try {
                        LOGGER.debug("Processing Request HTTP Version: {}", buffer);
                        request.setVersion(buffer.toString());
                    }catch (BadHttpVersionException e){

                    }
                    return;
                }
                else {
                    throw new CustomHttpException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if (_byte == SP){
                if (!methodParsed){
                    LOGGER.debug("Processing METHOD: {}", buffer);
                    request.setMethod(buffer.toString());
                    methodParsed = true;
                }
                else if(!requestedTargetParsed){
                    LOGGER.debug("Processing Request Target: {}", buffer);
                    request.setRequestTarget(buffer.toString());
                    requestedTargetParsed = true;
                }
                else {
                    throw new CustomHttpException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                buffer.delete(0, buffer.length());
            }else {
                buffer.append((char)_byte);
                if (!methodParsed) {
                    if (buffer.length() > 4) {
                        throw new CustomHttpException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }


    private void parseHeaders(InputStreamReader reader, HttpRequest request) throws  IOException {
        //String result = new BufferedReader(reader).lines().collect(Collectors.joining());
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        do {
            line = bufferedReader.readLine();
            sb.append(line);
        }while(!Objects.equals(line, "\r\n") && !Objects.equals(line, "\n") && !line.isEmpty());
        String[] result = sb.toString().split("\r\n");
        for (int i = 1; i < result.length; i++){
            String[] header = result[i].split(": ");
            request.setHeader(header[0], header[1]);
        }
    }

}
