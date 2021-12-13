package com.jasmin.httpserver.core;

import com.jasmin.http.BadHttpVersionException;
import com.jasmin.http.CustomHttpException;
import com.jasmin.http.HttpParser;
import com.jasmin.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.stream.Collectors;

public class HttpConnectionWorkerThread extends Thread{

    private final Socket socket;
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private final String path = "src/main/resources/htdocs";
    private String filename = "";



    public HttpConnectionWorkerThread(Socket socket) throws IOException {

        this.socket = socket;

    }

    @Override
    public void run() {

        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {

            //parsing http request
            HttpParser parser = new HttpParser();
            HttpRequest request;
            try {

                request = parser.parseHttpRequest(inputStream);
                filename = request.getRequestTarget();
                if (filename.equals("/")) filename = "/index.html";

            } catch (CustomHttpException e) {
                e.printStackTrace();
                filename = "/err400.html";
            } catch (BadHttpVersionException e) {
                e.printStackTrace();
                filename = "/err505.html";
            }
            String html;
            FileReader fileReader;
            if (!filename.endsWith(".html")) {
                filename = filename + ".html";
            }
            try {
                fileReader = new FileReader(path + filename);
                html = new BufferedReader(fileReader).lines().collect(Collectors.joining("\n"));
            } catch (FileNotFoundException e) {
                html = "<html>" +
                        "<head>" +
                        "    <title>ERROR 404</title>" +
                        "</head>" +
                        "<body>" +
                        "<h1>ERROR 404" +
                        "</h1>" +
                        "<h3>NOT FOUND</h3>" +
                        "</body>" +
                        "</html>";
            }


            final String CRLF = "\r\n"; // 13, 10

            String response =
                    "HTTP/1.1 200 OK" + CRLF + // Status Line  :   HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF + // HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());


            LOGGER.info(" * Connection Processing Finished.");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
